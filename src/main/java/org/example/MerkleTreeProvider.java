package org.example;


import org.bouncycastle.crypto.digests.Blake2sDigest;
import org.example.exception.EmptyTreeException;
import org.example.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerkleTreeProvider {

    private static final String PRINT_DIVIDER = "---";
    private static final Integer BLAKE_2_BITS = 16; // 16 bytes * 8 bits/byte = 128 bits output

    private final List<String> leaves;
    private final List<List<String>> treeLevels;
    private final Map<String, List<String>> proofStorage;

    public MerkleTreeProvider(List<String> leaves) throws NotFoundException {
        this.leaves = new ArrayList<>(leaves);
        this.treeLevels = new ArrayList<>();
        this.proofStorage = new HashMap<>();
        buildTree();
        generateProofs();
    }

    private void buildTree() {
        if(leaves.isEmpty()) return;

        treeLevels.clear();
        List<String> currentLevel = new ArrayList<>(leaves);
        treeLevels.add(currentLevel);

        while (currentLevel.size() > 1) {
            List<String> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                // Check if there is a sibling.
                if (i + 1 < currentLevel.size()) {
                    nextLevel.add(combineHashes(currentLevel.get(i), currentLevel.get(i + 1)));
                } else {
                    nextLevel.add(combineHashes(currentLevel.get(i), currentLevel.get(i)));
                }
            }
            currentLevel = nextLevel;
            treeLevels.add(currentLevel);
        }
    }

    private String hash(String data) {
        final Blake2sDigest digest = new Blake2sDigest(BLAKE_2_BITS);
        final byte[] hashBytes = new byte[digest.getDigestSize()];

        digest.update(data.getBytes(), 0, data.length());
        digest.doFinal(hashBytes, 0);

        return bytesToHex(hashBytes);
    }

    private String bytesToHex(byte[] bytes) {
        final StringBuilder hexString = new StringBuilder();

        for (byte oneByte : bytes) {
            final String hex = Integer.toHexString(0xff & oneByte);

            if (hex.length() == 1) {
                hexString.append('0'); // Making sure we have always length of 2 characters for bytes.
            }

            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getRoot() throws EmptyTreeException {
        if (treeLevels.isEmpty()) throw new EmptyTreeException("Merkle Tree is empty.");

        return treeLevels.get(treeLevels.size() - 1).get(0);
    }

    private void generateProofs() throws NotFoundException {
        for (String leaf : leaves) {
            proofStorage.put(leaf, generateProof(leaf));
        }
    }

    private List<String> generateProof(String leaf) throws NotFoundException {
        int index = leaves.indexOf(leaf);
        if (index == -1) {
            throw new NotFoundException("Leaf " + leaf + " is not part of the tree.");
        }

        final List<String> proof = new ArrayList<>();

        // Loop through each level of the tree except the root level
        for (int level = 0; level < treeLevels.size() - 1; level++) {

            final List<String> currentLevel = treeLevels.get(level);
            final int siblingIndex = (index % 2 == 0) ? index + 1 : index - 1; // even, sibling is the next element, previous otherwise

            if (siblingIndex < currentLevel.size()) { // Check if sibling is within the bound.
                proof.add(currentLevel.get(siblingIndex));
            } else {
                proof.add(currentLevel.get(index));
            }
            index /= 2;
        }
        return proof;
    }

    public List<String> getProof(String leaf) {
        return proofStorage.get(leaf);
    }

    public boolean verifyProof(String leaf, List<String> proof, String root) {
        String hash = leaf;

        for (String sibling : proof) {
            hash = combineHashes(hash, sibling);
        }

        return hash.equals(root);
    }

    public void insertLeaf(String leaf) throws NotFoundException {
        leaves.add(leaf);

        buildTree(); // I was trying to do an optimisation here but was too late so just rebuilding whole tree works well now.
        updateAllProofs();
    }

    public void updateLeaf(int index, String newLeaf) throws NotFoundException {
        if (index < 0 || index >= leaves.size()) {
            throw new IndexOutOfBoundsException("Invalid leaf index");
        }
        leaves.set(index, newLeaf);

        updateTreeAfterUpdate(index);
        updateAllProofs();
    }

    public void printMerkleTree() {
        printMerkleTree("");
    }

    public void printMerkleTree(String identifier) {
        System.out.println(PRINT_DIVIDER + identifier + PRINT_DIVIDER);
        System.out.println(leaves);
        System.out.println(treeLevels);
        System.out.println(proofStorage);
        System.out.println(PRINT_DIVIDER + identifier + PRINT_DIVIDER);
    }

    private void updateTreeAfterUpdate(int index) {
        String currentHash = leaves.get(index);

        for (List<String> currentLevel : treeLevels) {
            currentLevel.set(index, currentHash);

            // Check if the index is even
            if (index % 2 == 0) {
                // Check if there is an sibling.
                if (index + 1 < currentLevel.size()) {
                    currentHash = combineHashes(currentHash, currentLevel.get(index + 1));
                } else {
                    currentHash = combineHashes(currentHash, currentHash);
                }
            } else {
                currentHash = combineHashes(currentLevel.get(index - 1), currentHash);
            }

            index /= 2;
        }

        if (!treeLevels.get(treeLevels.size() - 1).contains(currentHash)) {
            treeLevels.get(treeLevels.size() - 1).add(currentHash);
        }
    }

    private void updateAllProofs() throws NotFoundException {
        for (String leaf : leaves) {
            proofStorage.put(leaf, generateProof(leaf));
        }
    }

    private String combineHashes(String hash1, String hash2) {
        if (hash1.compareTo(hash2) < 0) {
            return hash(hash1 + hash2);
        } else {
            return hash(hash2 + hash1);
        }
    }
}
