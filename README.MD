# MerkleTreeProvider

`MerkleTreeProvider` is a Java class that implements a Merkle Tree, a cryptographic tree structure that ensures data integrity and consistency. This implementation uses the Blake2s cryptographic hash function.

## Class Overview

### Fields

- `List<String> leaves`: Stores all the leaves of the Merkle Tree.
- `List<List<String>> treeLevels`: A list of lists that stores nodes at each level of the tree for efficient updates.
- `Map<String, List<String>> proofStorage`: A map that stores the proof for each leaf for quick retrieval.

### Constructors

- `MerkleTreeProvider(List<String> leaves)`: Initializes the Merkle Tree with the provided leaves and builds the tree.

### Methods

- `String getRoot()`: Returns the root of the Merkle Tree.
- `List<String> getProof(String leaf)`: Returns the proof for the specified leaf.
- `boolean verifyProof(String leaf, List<String> proof, String root)`: Verifies the proof for the specified leaf against the given root.
- `void insertLeaf(String leaf)`: Inserts a new leaf into the Merkle Tree.
- `void updateLeaf(int index, String newLeaf)`: Updates an existing leaf in the Merkle Tree.
- `void printMerkleTree()`: Prints the structure of the Merkle Tree.
- `void printMerkleTree(String identifier)`: Prints the structure of the Merkle Tree with an identifier.

### Private Methods

- `void buildTree()`: Builds the entire Merkle Tree.
- `String hash(String data)`: Hashes the provided data using Blake2s.
- `String bytesToHex(byte[] bytes)`: Converts a byte array to a hexadecimal string.
- `void generateProofs()`: Generates proofs for all leaves in the tree.
- `List<String> generateProof(String leaf)`: Generates a proof for the specified leaf.
- `void updateTreeAfterUpdate(int index)`: Updates the tree after a leaf is modified.
- `void updateAllProofs()`: Updates proofs for all leaves.
- `String combineHashes(String hash1, String hash2)`: Combines two hashes in lexicographical order.

## Running Tests

### Test Class

The provided test class `MerkleTreeTest` uses JUnit 5 for testing the `MerkleTreeProvider` class. This class is already included in the codebase.

### Running Tests in Console

1. **Compile the Classes**:
   Ensure you have the Bouncy Castle and JUnit libraries in your classpath. Compile the classes using the following command:

   ```sh
   mvn test
