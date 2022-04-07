package lesson2;

public class ExceptionApp {
    public static void main(String[] args) {
        int arraySize = 4;
        String[][] strArrayTest = {{"0", "1", "2"}, {"o", "1", "2"}, {"0", "1", "2"}};
        try {
            String[][] strArray = initArray(arraySize);
            printArray(strArray);
            int[][] intArray1 = convertStrToInt(strArray, strArray.length);
            printArray(intArray1);
            sumOfArrayElem(intArray1);
            int[][] intArray2 = convertStrToInt(strArrayTest, strArrayTest.length);
            printArray(intArray2);
            sumOfArrayElem(intArray2);
        } catch (MyArraySizeException e) {
            System.out.println("Массив не корректного размера.");
            e.printStackTrace(System.out);
        } catch (MyArrayDataException e) {
            System.out.println("Массив для преобразования состоит не из String");
            e.printStackTrace(System.out);
        } catch (NumberFormatException e) {
            System.out.println("В массив String записан элемент, который нельзя преобразовать в int.");
            e.printStackTrace(System.out);
        }

    }

    private static void sumOfArrayElem(int[][] intArray) {
        int sum = 0;
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray.length; j++) {
                sum += intArray[i][j];
            }
        }
        System.out.println("Сумма элементов массива int массива = " + sum);
        System.out.println();
    }

    private static int[][] convertStrToInt(String[][] strArray, int arraySize) throws MyArrayDataException {
        int[][] intArray = new int[arraySize][arraySize];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray.length; j++) {
                if (strArray[i][j] instanceof String) {
                    intArray[i][j] = Integer.parseInt(strArray[i][j]);
                } else {
                    throw new MyArrayDataException("Массив, который требуется преобразовать в int, состоит не из String");
                }

            }
        }
        return intArray;
    }

    private static void printArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static String[][] initArray(int arraySize) throws MyArraySizeException {
        if (arraySize > 4 || arraySize <= 0) {
            throw new MyArraySizeException("Массив не может быть размера: " + arraySize + "x" + arraySize);
        }
        String[][] array = new String[arraySize][arraySize];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                array[i][j] = "" + i + j;
            }
        }
        return array;
    }
}
