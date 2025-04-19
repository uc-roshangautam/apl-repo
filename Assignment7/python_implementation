from collections import Counter

class StatisticsCalculator:
    def __init__(self, numbers):
        self.numbers = numbers

    def mean(self):
        return sum(self.numbers) / len(self.numbers)

    def median(self):
        sorted_nums = sorted(self.numbers)
        n = len(sorted_nums)
        mid = n // 2
        if n % 2 == 0:
            return (sorted_nums[mid - 1] + sorted_nums[mid]) / 2
        else:
            return sorted_nums[mid]

    def mode(self):
        freq = Counter(self.numbers)
        max_freq = max(freq.values())
        return [num for num, count in freq.items() if count == max_freq]

if __name__ == "__main__":
    data = [23, 45, 12, 67, 89, 23, 45, 78, 34, 56]
    print(f"Data: {data}")
    
    stats = StatisticsCalculator(data)
    
    print("Mean:", round(stats.mean(), 2))
    print("Median:", round(stats.median(), 2))
    print("Mode:", stats.mode())