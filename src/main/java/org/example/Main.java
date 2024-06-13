package org.example;

import org.example.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    /**
     * Example usage of the MerkleTreeProvider. More cases in tests.
     */
    public static void main(String[] args) throws NotFoundException {
        final List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        final MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        merkleTree.printMerkleTree();
    }
}