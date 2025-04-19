#include <stdio.h>
#include <stdlib.h>

int compare(const void *a, const void *b) {
    return (*(int*)a - *(int*)b);
}

float mean(int arr[], int n) {
    int sum = 0;
    for (int i = 0; i < n; i++)
        sum += arr[i];
    return (float)sum / n;
}

float median(int arr[], int n) {
    qsort(arr, n, sizeof(int), compare);
    if (n % 2 == 0)
        return (arr[n/2 - 1] + arr[n/2]) / 2.0;
    else
        return arr[n/2];
}

void mode(int arr[], int n) {
    int maxCount = 0;
    int modeCount = 0;

    for (int i = 0; i < n; i++) {
        int count = 1;
        for (int j = i+1; j < n; j++) {
            if (arr[i] == arr[j])
                count++;
        }

        if (count > maxCount) {
            maxCount = count;
        }
    }

    printf("Mode: ");
    for (int i = 0; i < n; i++) {
        int count = 1;
        for (int j = i+1; j < n; j++) {
            if (arr[i] == arr[j])
                count++;
        }

        if (count == maxCount) {
            int seen = 0;
            for (int k = 0; k < i; k++) {
                if (arr[k] == arr[i]) {
                    seen = 1;
                    break;
                }
            }
            if (!seen) {
                printf("%d ", arr[i]);
            }
        }
    }
    printf("\n");
}

void print_array(int arr[], int n) {
    printf("Data: [");
    for (int i = 0; i < n; i++) {
        printf("%d", arr[i]);
        if (i < n - 1) {
            printf(", ");
        }
    }
    printf("]\n");
}

int main() {
    int arr[] = {23, 45, 12, 67, 89, 23, 45, 78, 34, 56};
    int n = sizeof(arr) / sizeof(arr[0]);

    print_array(arr, n);
    printf("Mean: %.2f\n", mean(arr, n));
    printf("Median: %.2f\n", median(arr, n));
    mode(arr, n);

    return 0;
}
