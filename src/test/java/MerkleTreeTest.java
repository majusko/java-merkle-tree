import org.example.exception.EmptyTreeException;
import org.example.MerkleTreeProvider;
import org.example.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MerkleTreeTest {

    @Test
    public void testMerkleRoot() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);
        String expectedRoot = merkleTree.getRoot();
        assertNotNull(expectedRoot);

        merkleTree.printMerkleTree("testMerkleRoot");
    }

    @Test
    public void testMerkleProof() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        List<String> proof = merkleTree.getProof("block2");
        assertNotNull(proof);

        boolean isValid = merkleTree.verifyProof("block2", proof, merkleTree.getRoot());
        assertTrue(isValid);

        merkleTree.printMerkleTree("testMerkleProof");
    }

    @Test
    public void testInsertLeaf() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4", "block5"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        merkleTree.insertLeaf("block6");
        String newRoot = merkleTree.getRoot();
        assertNotNull(newRoot);

        List<String> proof = merkleTree.getProof("block6");
        assertNotNull(proof);

        boolean isValid = merkleTree.verifyProof("block6", proof, newRoot);
        assertTrue(isValid);

        merkleTree.printMerkleTree("testInsertLeaf");
    }

    @Test
    public void testUpdateLeaf() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4", "block5"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        merkleTree.updateLeaf(1, "block4_updated");
        String updatedRoot = merkleTree.getRoot();
        assertNotNull(updatedRoot);

        List<String> proof = merkleTree.getProof("block4_updated");
        assertNotNull(proof);

        boolean isValid = merkleTree.verifyProof("block4_updated", proof, updatedRoot);
        assertTrue(isValid);

        merkleTree.printMerkleTree("testUpdateLeaf");
    }

    @Test
    public void testInvalidProof() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4", "block5"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        List<String> proof = merkleTree.getProof("block2");
        assertNotNull(proof);

        boolean isValid = merkleTree.verifyProof("block4", proof, merkleTree.getRoot());
        assertFalse(isValid);

        merkleTree.printMerkleTree("testInvalidProof");
    }

    @Test
    public void testEmptyTreeInitialization() throws NotFoundException, EmptyTreeException {
        List<String> leaves = new ArrayList<>();
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        assertThrows(EmptyTreeException.class, merkleTree::getRoot);
    }

    @Test
    public void testGetProofForNonExistentLeaf() throws NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        assertNull(merkleTree.getProof("block5"));
    }

    @Test
    public void testUpdateLeafWithInvalidIndex() throws NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            merkleTree.updateLeaf(10, "block10");
        });
    }

    @Test
    public void testVerifyProofWithInvalidRoot() throws EmptyTreeException, NotFoundException {
        List<String> leaves = new ArrayList<>(Arrays.asList("block1", "block2", "block3", "block4"));
        MerkleTreeProvider merkleTree = new MerkleTreeProvider(leaves);

        List<String> proof = merkleTree.getProof("block2");
        assertNotNull(proof);

        boolean isValid = merkleTree.verifyProof("block2", proof, "invalid_root");
        assertFalse(isValid);

        merkleTree.printMerkleTree("testVerifyProofWithInvalidRoot");
    }
}
