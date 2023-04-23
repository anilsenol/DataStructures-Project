import java.io.File;
import java.util.*;

public class SeriesCharacter {
   
    public int numberOfVertices;
    public final Map<String, Integer> hashes = new HashMap<>();
    public String[] elements;
    public int[][] adjacencyMatrix;

    
    public void Operations() {
        Scanner s = new Scanner(System.in);

        
        ReadGraphFromFile();
        String name1, name2;

        
        System.out.println("Enter Name 1:");
        name1 = "Meryn";
        System.out.println("Enter Name 2:");
        name2 = "Elia";
        if (0 < IsThereAPath(name1, name2)){
            System.out.println("Yes, There is a Path(“" + name1 + "”, “" + name2 + "”): true");
        } else {
            System.out.println("No, There is no Path(“" + name1 + "”, “" + name2 + "”): false");
        }

        
        System.out.println("Enter the path length:");
        int pathlength = s.nextInt();
        System.out.println("Enter the number of vertices:");
        int numberOfVertices = s.nextInt();
        System.out.println("Enter the Name:");
        name1 = s.next();

        ArrayList<List<Integer>> paths = AllPathsShorterThanEqualTo(pathlength, numberOfVertices, name1);
        System.out.println("All PathsShorterThanEqualTo("+pathlength+", "+numberOfVertices+", " +name1 +")");
        for (List<Integer> path : paths) {
            for (int t = 0; t < path.size() - 1; t++) {
                System.out.print(elements[path.get(t)] + ",");
            }
            System.out.println();
        }

        
        System.out.println("Enter Name 1:");
        name1 = s.next();
        System.out.println("Enter Name 2:");
        name2 = s.next();
        System.out.print("ShortestPathLengthFromTo(“" + name1 + "”, “" + name2 + "”):");
        int length = ShortestPathLengthFromTo(name1, name2);
        if (Integer.MAX_VALUE == length) {
            System.out.println("infinity");
        } else {
            System.out.println(length);
        }

        
        System.out.println("Enter the 1st Name:");
        name1 = s.next();
        System.out.println("Enter the 2nd Name:");
        name2 = s.next();
        System.out.print("NoOfPathsFromTo(“" + name1 + "”, “" + name2 + "”):");
        int number = NoOfPathsFromTo(name1, name2);
        System.out.println(number);

        
        System.out.println("Enter the 1st Name:");
        name1 = s.next();
        System.out.println("Enter the 2nd Name:");
        name2 = s.next();
        System.out.print("DFSfromTo(“" + name1 + "”, “" + name2 + "”):");
        List<Integer> edges = BFSfromTo(name1, name2);
        for (Integer edge : edges) {
            System.out.print(elements[edge] + ",");
        }
        System.out.println();

        
        System.out.println("Enter the 1st Name:");
        name1 = s.next();
        System.out.println("Enter the 2nd Name:");
        name2 = s.next();
        name1 = "A"; name2 = "E";
        System.out.print("DFSfromTo(“" + name1 + "”, “" + name2 + "”):");
        edges = DFSfromTo(name1, name2);
        for (Integer edge : edges) {
            System.out.print(elements[edge] + ",");
        }
        System.out.println();

        
        System.out.println("Enter the Name:");
        name1 = s.next();
        System.out.print("NoOfPathsFromTo(“" + name1 + "”):");
        number = NoOfVerticesInComponent(name1);
        System.out.println(number);

    }

    
    public void ReadGraphFromFile() {
        try {
            File file = new File("got-edges.txt");
            Scanner nFile = new Scanner(file);

            ArrayList<String> characters = new ArrayList<>();
            while (nFile.hasNextLine()) {
                String line = nFile.nextLine();
                String[] splitline = line.split(",");
                if (splitline.length != 3) {
                    System.out.println(line);
                    continue;
                }
                if (!characters.contains(splitline[0])) {
                    characters.add(splitline[0]);
                }
                if (!characters.contains(splitline[1])) {
                    characters.add(splitline[1]);
                }
            }
            nFile.close();

            numberOfVertices = characters.size();
            adjacencyMatrix = new int[numberOfVertices][numberOfVertices];
            elements = new String[numberOfVertices];

            for (int i=0; i<numberOfVertices; i++) {
                elements[i] = characters.get(i);
                hashes.put(elements[i], i);
            }

            nFile = new Scanner(file);
            while (nFile.hasNextLine()) {
                String line = nFile.nextLine();
                String[] splitline = line.split(",");
                if (splitline.length != 3) {
                    System.out.println(line);
                    continue;
                }

                int i = hashes.get(splitline[0]);
                int j = hashes.get(splitline[1]);
                int firstHash = Integer.parseInt(splitline[2]);
                adjacencyMatrix[i][j] = firstHash;
                adjacencyMatrix[j][i] = firstHash;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Data were readed.");
    }

    
    private int IsThereAPath(String name1, String name2) {
        int firstHash = hashes.get(name1);
        int lastHash = hashes.get(name2);

        return IsThereAPath(firstHash, lastHash, 0);
    }

    
    private int IsThereAPath(int name1, int name2, int iStart) {
        int a = isConnected(name1, name2);
        if (0 < a) {
            return a;
        }

        for (int i = iStart; i < numberOfVertices; i++) {
            a = isConnected(name1, i);
            if (0 < a) {
                return IsThereAPath(i, name2, i);
            }
        }
        return 0;
    }

    
    private int isConnected(int name1, int name2) {
        try {
            return adjacencyMatrix[name1][name2];
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    
    private ArrayList<List<Integer>> AllPathsShorterThanEqualTo(int iPathLen, int VertexNo, String name1) {
        int iV = hashes.get(name1);

        ArrayList<List<Integer>> edges = new ArrayList<>();
        for (int t = 0; t < VertexNo; t++) {
            int a = adjacencyMatrix[iV][t];
            if (a > 0) {
                List<Integer> vertexList = new ArrayList<>();
                vertexList.add(t);
                edges.add(vertexList);
            }
        }

        edges = FindPath(edges, iV);

        ArrayList<Integer> longpathlist = new ArrayList<>();
        for (List<Integer> edgeList : edges) {
            int sum = 0;
            int j = iV;
            for (int i : edgeList) {
                sum += adjacencyMatrix[j][i];
                j = i;
            }
            longpathlist.add(sum);
        }

        ArrayList<List<Integer>> newEdgeList = new ArrayList<>();
        int i = 0;
        for (Object edgelist : edges) {
            List<Integer> vertexList = (List<Integer>) edgelist;
            if (longpathlist.get(i) <= iPathLen) {
                if (vertexList.size() - 1 <= VertexNo) {
                    newEdgeList.add(vertexList);
                }
            }
            i++;
        }
        return newEdgeList;
    }

    
    private ArrayList<List<Integer>> FindPath(ArrayList<List<Integer>> edgelist, int hash) {
        ArrayList<List<Integer>> newEdges = new ArrayList<>();
        boolean a = false;
        for (Object edgeList : edgelist) {
            List<Integer> vertexList = (List<Integer>) edgeList;
            int hashValue = vertexList.get(vertexList.size() - 1);
            if (hashValue == hash) {
                newEdges.add(vertexList);
                continue;
            }
            for (int i = 0; i < numberOfVertices; i++) {
                int iW = adjacencyMatrix[hashValue][i];
                if (iW > 0) {
                    if (i == hash) {
                        if (vertexList.size() < 2) {
                            continue;
                        } else {
                            List<Integer> newVertexList = new ArrayList<>(vertexList);
                            newVertexList.add(i);
                            newEdges.add(newVertexList);
                            continue;
                        }
                    }
                    if (!vertexList.contains(i)) {
                        List<Integer> newVertexList = new ArrayList<>(vertexList);
                        newVertexList.add(i);
                        newEdges.add(newVertexList);
                        a = true;
                    }
                }
            }
        }
        edgelist = newEdges;
        if (a) {
            edgelist = FindPath(edgelist, hash);
        }

        return edgelist;
    }

    
    private int ShortestPathLengthFromTo(String name1, String name2) {
        int firstHash = hashes.get(name1);
        int lastHash = hashes.get(name2);
        int iShortestLen = Integer.MAX_VALUE;
        for (int i = 0; i < hashes.size(); i++) {
            for (int j = 0; j < hashes.size(); j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    if (i == firstHash && j == lastHash) {
                        iShortestLen = adjacencyMatrix[i][j];
                    }
                    if (adjacencyMatrix[i][j] < iShortestLen && iShortestLen != Integer.MAX_VALUE) {
                        iShortestLen = adjacencyMatrix[i][j];
                    }
                }
            }
        }
        return iShortestLen;
    }

    
    private int NoOfVerticesInComponent(String name) {
        int firstHash = hashes.get(name);

        ArrayList<List<Integer>> sVertexList = AllPathsShorterThanEqualTo(Integer.MAX_VALUE, Integer.MAX_VALUE, name);

        Set<Integer> uniqueVertices = new HashSet<>();
        for (List<Integer> vertexList : sVertexList) {
            for (int t : vertexList) {
                uniqueVertices.add(t);
            }
        }

        int count = 0;
        for (int vertexHash : uniqueVertices) {
            if (vertexHash == firstHash) {
                count++;
            }
        }
        return count;
    }

    
    private int NoOfPathsFromTo(String name1, String name2) {
        int firstHash = hashes.get(name1);
        int LastHash = hashes.get(name2);

        ArrayList<List<Integer>> sEdgeList = AllPathsShorterThanEqualTo(Integer.MAX_VALUE, Integer.MAX_VALUE, name1);

        int count = 0;
        for (Object edgelist : sEdgeList) {
            List<Integer> vertexList = (List<Integer>) edgelist;
            for (int t : vertexList) {
                if (LastHash == t) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    
    private List<Integer> BFSfromTo(String name1, String name2) {
        int firstHash = hashes.get(name1);
        int lastHash = hashes.get(name2);

        ArrayList<Integer> edgeList = new ArrayList<>();
        Queue<Integer> vertices = new LinkedList<>();
        boolean[] checked = new boolean[numberOfVertices];

        checked[firstHash] = true;
        vertices.add(firstHash);

        while (!vertices.isEmpty()) {
            int s = vertices.poll();
            edgeList.add(s);
            boolean a = false;
            for (int t = 0; t < numberOfVertices; t++) {
                int weight = adjacencyMatrix[s][t];
                if (weight > 0) {
                    if (lastHash == t) {
                        a = true;
                        edgeList.add(t);
                        break;
                    }
                    if (!checked[t]) {
                        checked[t] = true;
                        vertices.add(t);
                    }
                }
            }
            if (a) {
                break;
            }
        }

        return edgeList;
    }


    
    private List<Integer> DFSfromTo(String name1, String name2) {
        int firstHash = hashes.get(name1);
        int lastHash = hashes.get(name2);

        boolean[] checked = new boolean[numberOfVertices];
        ArrayList<Integer> edgelist = new ArrayList<>();
        return DFSfromTo(firstHash, lastHash, checked, edgelist);
    }

    
    private List<Integer> DFSfromTo(int firstHash, int lastHash, boolean[] checked, ArrayList<Integer> edgelist) {
        checked[firstHash] = true;
        edgelist.add(firstHash);

        for (int t = 0; t < numberOfVertices; t++) {
            int weight = adjacencyMatrix[firstHash][t];
            if (weight > 0) {
                if (lastHash == t) {
                    edgelist.add(t);
                    return edgelist;
                }
                if (!checked[t]) {
                    List<Integer> path = DFSfromTo(t, lastHash, checked, edgelist);
                    if(path != null) {
                      return edgelist;
                    }
                }
            }
        }
        edgelist.remove(edgelist.size()-1);
        return null;
    }

    
    
    public static void main(String[] args) {
        SeriesCharacter seriesCharacter = new SeriesCharacter();
        seriesCharacter.Operations();
    }
}