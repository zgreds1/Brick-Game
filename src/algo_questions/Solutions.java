package algo_questions;

import java.util.Arrays;

public class Solutions {
    public static int alotStudyTime(int[] tasks, int[] timeSlots){
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);

        int currTaskIndex = tasks.length - 1;
        int counter = 0;
        int currSlotIndex = timeSlots.length - 1;
        while (currTaskIndex >= 0 && currSlotIndex >= 0){
            if (timeSlots[currSlotIndex] >= tasks [currTaskIndex]){
                counter++;
                currSlotIndex--;
            }
            currTaskIndex--;
        }
        return counter;
    }

    public static int minLeap(int[] leapNum){
        int numOfPads = leapNum.length;
        int[] leapsFromPad = new int[numOfPads];

        // base case
        leapsFromPad[numOfPads - 1] = 0;

        for (int i = numOfPads - 2; i >= 0 ; i--) {
            int min = leapsFromPad[i + 1];
            for (int j = 2; j <= leapNum[i] && j + i < numOfPads; j++) {
                 if (leapsFromPad[j + i] < min){
                     min = leapsFromPad[j + i];
                 }
            }
            min++;
            leapsFromPad[i] = min;
        }
        return leapsFromPad[0];
    }

    public static int bucketWalk(int n)
    {
        // base case
        int waysToFillPrevious = 1;
        int waysToFillCurrent = 1;

        int tmp;

        for (int i = 2; i <= n; i++) {
            tmp = waysToFillPrevious;
            waysToFillPrevious = waysToFillCurrent;
            waysToFillCurrent += tmp;
        }
        return waysToFillCurrent;
    }

    public static int numTrees(int n){
        if (n == 0){
            return 1;
        }
        int[] numTreesArr = new int[n + 1];

        // base case
        numTreesArr[0] = 1;
        numTreesArr[1] = 1;

        for (int i = 2; i <= n; i++){
            for (int j = 1; j <= i; j++){
                // n-i in right * i-1 in left
                numTreesArr[i] += (numTreesArr[i - j] * numTreesArr[j - 1]);
            }
        }

        return numTreesArr[n];
    }

}
