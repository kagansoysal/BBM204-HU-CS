import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    private int ccNum = 0;

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        HashMap<String, Molecule> moleculeMap = new HashMap<>();
        for (Molecule molecule : molecules) moleculeMap.put(molecule.getId(), molecule);

        for (Molecule molecule : molecules)
            if (!molecule.visited) {
                molecule.cc = ccNum;
                dfs(molecule, moleculeMap);
            }

        for (int i = 0; i < ccNum; i++) structures.add(new MolecularStructure());

        molecules.forEach(molecule -> {
            structures.get(molecule.cc).addMolecule(molecule);
            molecule.visited = false;
        });

        return structures;
    }

    private void dfs(Molecule molecule, HashMap<String, Molecule> moleculeMap) {
        molecule.visited = true;

        for (String id : molecule.getBonds()) {
            Molecule adj = moleculeMap.get(id);
            if (adj.cc != -1) molecule.cc = adj.cc;
            else adj.cc = molecule.cc;

            if (!adj.visited) dfs(adj, moleculeMap);
        }
        if (molecule.cc == ccNum) ccNum++;
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 1; i <= molecularStructures.size(); i++) System.out.println("Molecules in Molecular Structure " + i + ": " + molecularStructures.get(i - 1));
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        for (MolecularStructure structure : targetStructures) if (!sourceStructures.contains(structure)) anomalyList.add(structure);
        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) System.out.println(structure);
    }
}