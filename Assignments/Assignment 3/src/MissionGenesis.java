import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        List<Molecule> humanMolecules = new ArrayList<>();
        List<Molecule> vitalesMolecules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String id = null;
            int bondStrength = 0;
            List<String> bonds = new ArrayList<>();
            boolean humanData = true;

            while ((line = br.readLine()) != null) {
                if (line.contains("<ID>")) {
                    id = line.substring(line.indexOf('M'), line.lastIndexOf('<'));
                } else if (line.contains("Strength")) {
                    bondStrength = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
                } else if (line.contains("MoleculeID")) {
                    bonds.add(line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')));
                } else if (line.contains("VitalesMolecularData")) {
                    humanData = false;
                } else if (line.contains("</Molecule>")) {
                    List<Molecule> targetList = humanData ? humanMolecules : vitalesMolecules;
                    targetList.add(new Molecule(id, bondStrength, new ArrayList<>(bonds)));
                    bonds.clear();
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        molecularDataHuman = new MolecularData(humanMolecules);
        molecularDataVitales = new MolecularData(vitalesMolecules);
    }
}
