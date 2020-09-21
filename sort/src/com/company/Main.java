package com.company;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main
{

    public static void main(String[] args) throws Exception {
        /*for (int i=0; i < args.length; i++) {
            System.out.println(i + "=" + args[i]);
        }*/

        String sortMode = getSortMode(args);
        String dataType = getDataType(args);
        String outputFileName = getOutputFileName(args);
        String [] inputFileNames = getInputFileName(args);


        List<Object> finalArrayList = new ArrayList();


        for (String inputFileName: inputFileNames)
                {
                    if (finalArrayList.addAll(readFromFile(inputFileName, dataType))) {
                        sortUnsorted(finalArrayList, 0, finalArrayList.size() - 1, sortMode, dataType);
                    }

                }

        saveToFile(outputFileName, finalArrayList);
    }


    //проверка строки - число это или нет
    public static boolean isNumber(String s) {
        if (s.length() == 0) {
            return false;
        }
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((i != 0 && c == '-')
                    || (!Character.isDigit(c) && c != '-')
                    || (chars.length == 1 && c == '-'))
                 {
                return false;
            }
        }
        return true;
    }

    //считать строки из файла
    public static List<Object> readFromFile (String inputFileName, String dataType) throws  IOException {
        List<Object> arrayList = new ArrayList<>();
        File file = new File (inputFileName);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            try {
                String line;
                if (dataType.equalsIgnoreCase("int")) {
                    while ((line = reader.readLine()) != null && isNumber(line)) {
                        arrayList.add(Integer.parseInt(line));
                    }
                } else {
                    while ((line = reader.readLine()) != null) {
                        arrayList.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    return arrayList;
// пишем обработку исключения при закрытии потока чтения
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return arrayList;
    }

    
    public  static String getSortMode (String [] args) {
        String sortMode = null;
        if (args [0].equalsIgnoreCase("-a")) {
            sortMode = "ascend";
        } else if (args [0].equalsIgnoreCase("-d")) {
            sortMode = "descend";
        } else {
            sortMode = "ascend";
        }
        return sortMode;
    }

    public static String getDataType (String [] args) {
        String dataType = null;
        if (args [0].equalsIgnoreCase("-a")  || args [0].equalsIgnoreCase("-d")) {
            if (args [1].equalsIgnoreCase("-i")) {
                dataType = "int";
            } else {
                dataType = "String";
            }
        } else {
            if (args [0].equalsIgnoreCase("-i")) {
                dataType = "int";
            } else {
                dataType = "String";
            }
        }
        return dataType;
    }

    public static String getOutputFileName (String [] args) {
        String outputFileName = null;
        if (args [0].equalsIgnoreCase("-a")  || args [0].equalsIgnoreCase("-d")) {
            outputFileName = args [2];
        } else {
            outputFileName = args [1];
        }
        return outputFileName;
    }

    public static String [] getInputFileName (String [] args) {
        if (args [0].equalsIgnoreCase("-a")  || args [0].equalsIgnoreCase("-d")) {
            String [] inputFileNames = new String [args.length - 3];
            for(int i = 0; i < args.length - 3; i ++)
                inputFileNames[i] = args[i + 3];
            return inputFileNames;
        } else {
            String [] inputFileNames = new String [args.length - 2];
            for(int i = 0; i < args.length - 2; i ++)
                inputFileNames[i] = args[i + 2];
            return inputFileNames;
        }

    }

    //Сравнение строк
    public static boolean isCompared (Object a, Object b, String sortMode, String dataType) {
        if (sortMode.equalsIgnoreCase("ascend") && dataType.equalsIgnoreCase("String")) {
            return a.toString().compareTo(b.toString()) < 0;
        } else if (sortMode.equalsIgnoreCase("ascend") && dataType.equalsIgnoreCase("int")) {
            return (Integer)a < (Integer)b;
        } else if (sortMode.equalsIgnoreCase("descend") && dataType.equalsIgnoreCase("String")) {
            return a.toString().compareTo(b.toString()) > 0;
        } else if (sortMode.equalsIgnoreCase("descend") && dataType.equalsIgnoreCase("int")) {
            return (Integer)a > (Integer)b;
        } else {
            return false;
        }
    }

    //сохранить эррейлист в файл
    public static void saveToFile (String outputFileName, List<Object> arrayList) throws IOException {
        try (BufferedWriter writter = new BufferedWriter(new FileWriter(outputFileName))) {
            for (Object value : arrayList) {
                writter.write(value + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //отсортировать эррей лист
    private static void sortUnsorted(List<Object> arrayList, int lowIndex, int highIndex, String sortMode, String dataType) throws Exception {
        if (highIndex<=lowIndex)
            return;
        int middleIndex = lowIndex + (highIndex - lowIndex) / 2;
        sortUnsorted(arrayList, lowIndex, middleIndex, sortMode, dataType);
        sortUnsorted(arrayList, middleIndex + 1, highIndex, sortMode, dataType);

        List <Object> buf = new ArrayList<>(arrayList);

        int i = lowIndex, j = middleIndex + 1;
        for (int k = lowIndex; k <= highIndex; k++) {
            if (i > middleIndex) {
                arrayList.set(k, buf.get(j));
                j++;
            } else if (j > highIndex) {
                arrayList.set(k, buf.get(i));
                i++;
            } else if (isCompared(buf.get(j), buf.get(i), sortMode, dataType)) {
                arrayList.set(k, buf.get(j));
                j++;
            } else {
                arrayList.set(k, buf.get(i));
                i++;
            }
        }

    }

}


