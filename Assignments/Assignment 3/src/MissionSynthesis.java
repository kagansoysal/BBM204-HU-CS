import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans
    private final ArrayList<Molecule> selectedMolecule = new ArrayList<>();

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        for (MolecularStructure structure : humanStructures) selectedMolecule.add(structure.getMoleculeWithWeakestBondStrength());
        for (MolecularStructure structure : diffStructures) selectedMolecule.add(structure.getMoleculeWithWeakestBondStrength());

        ArrayList<Bond> potentialBonds = new ArrayList<>();

        for (int i = 0; i < selectedMolecule.size(); i++)
            for (int j = i + 1; j < selectedMolecule.size(); j++) {
                boolean isFirstMolecule = selectedMolecule.get(i).compareTo(selectedMolecule.get(j)) < 0;

                Molecule from = selectedMolecule.get(isFirstMolecule ? i : j);
                Molecule to = selectedMolecule.get(isFirstMolecule ? j : i);

                Double weight = (from.getBondStrength() + to.getBondStrength()) / 2.0;
                potentialBonds.add(new Bond(to, from, weight));
            }

        Comparator<Bond> comparator = Comparator.comparingDouble(Bond::getWeight);
        potentialBonds.sort(comparator);

        potentialBonds.forEach(bond -> {
            if (!connect(bond.getFrom(), bond.getTo())) {
                bond.getFrom().adj.add(bond.getTo());
                bond.getTo().adj.add(bond.getFrom());
                serum.add(bond);
            }
            for (Molecule molecule : selectedMolecule) molecule.visited = false;
        });
        return serum;
    }

    private boolean connect(Molecule from, Molecule to){
        return dfs(from, to);
    }

    private boolean dfs(Molecule molecule, Molecule to) {
        if (molecule.equals(to)) return true;
        molecule.visited = true;
        return molecule.adj.stream().anyMatch(adj -> !adj.visited && dfs(adj, to));
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {
        ArrayList<Molecule> humanSelected = new ArrayList<>();
        ArrayList<Molecule> vitalesSelected = new ArrayList<>();

        selectedMolecule.forEach(molecule -> {
            for (MolecularStructure structure : humanStructures) if (structure.hasMolecule(molecule.getId())) humanSelected.add(molecule);
            for (MolecularStructure structure : diffStructures) if (structure.hasMolecule(molecule.getId())) vitalesSelected.add(molecule);
        });

        System.out.println("Typical human molecules selected for synthesis: " + humanSelected);
        System.out.println("Vitales molecules selected for synthesis: " + vitalesSelected);
        System.out.println("Synthesizing the serum...");

        for (Bond bond : serum) System.out.printf("Forming a bond between %s - %s with strength %.2f%n", bond.getFrom(), bond.getTo(), bond.getWeight());

        double totalStrength = serum.stream().mapToDouble(Bond::getWeight).sum();
        System.out.printf("The total serum bond strength is %.2f%n", totalStrength);
    }
}