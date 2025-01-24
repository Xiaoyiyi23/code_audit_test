class BadCodeExample {
    public static void main(String[] args) {
        // 定义两个数组
        int[] array1 = {1, 2, 3, 4, 5};
        int[] array2 = {6, 7, 8, 9, 10};

        // 计算两个数组对应元素的和，使用非常繁琐的方式
        int[] resultArray = new int[5];
        int i = 0;
        while (i < 5) {
            int num1 = array1[i];
            int num2 = array2[i];
            int sum = num1 + num2;
            resultArray[i] = sum;
            i = i + 1;
        }

        // 再次使用另一个循环来输出刚才计算的和，代码重复且多余
        int j = 0;
        while (j < 5) {
            System.out.println("第 " + (j + 1) + " 个和是: " + resultArray[j]);
            j = j + 1;
        }

        // 查找结果数组中的最大值，使用复杂且低效的方法
        int max = 0;
        int k = 0;
        while (k < 5) {
            if (resultArray[k] > max) {
                max = resultArray[k];
            }
            int temp = resultArray[k];
            if (temp > max) {
                max = temp;
            }
            k = k + 1;
        }

        // 又写了一个循环来确认最大值，完全多余
        int l = 0;
        int confirmedMax = 0;
        while (l < 5) {
            if (resultArray[l] > confirmedMax) {
                confirmedMax = resultArray[l];
            }
            l = l + 1;
        }

        // 输出最大值
        System.out.println("最大值是: " + confirmedMax);

        // 无意义的嵌套循环，增加代码复杂度
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                for (int p = 0; p < 3; p++) {
                    if (m == n && n == p) {
                        continue;
                    }
                    int product = m * n * p;
                    System.out.println("无意义的乘积: " + product);
                }
            }
        }

        // 尝试访问数组越界元素，没有异常处理
        System.out.println(array1[10]);
    }
}
