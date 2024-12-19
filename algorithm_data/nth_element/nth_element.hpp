#include <algorithm>
#include <ranges>
#include <iterator>

namespace ranges = std::ranges;

template <ranges::random_access_range Range, typename Comp>
void nthElement(Range& range, typename Range::difference_type n, Comp comp) {
    auto begin = std::begin(range);
    auto end = std::end(range);

    if (n >= 0 && n < std::distance(begin, end)) {
        auto partition = [&](auto first, auto last, auto pivot) {
            auto pivotValue = *pivot;
            std::iter_swap(pivot, std::prev(last)); // Move pivot to the end
            auto store = first;
            for (auto it = first; it != std::prev(last); ++it) {
                if (comp(*it, pivotValue)) {
                    std::iter_swap(store, it);
                    ++store;
                }
            }
            std::iter_swap(store, std::prev(last)); // Move pivot to its final position
            return store;
        };

        auto left = begin;
        auto right = end;

        while (left != right) {
            auto pivot = left + (std::distance(left, right) / 2); // Choose middle as pivot
            auto partitionPoint = partition(left, right, pivot);

            if (partitionPoint == begin + n) {
                return; // The nth element is now at its correct position
            } else if (partitionPoint < begin + n) {
                left = partitionPoint + 1; // Narrow down to the right part
            } else {
                right = partitionPoint; // Narrow down to the left part
            }
        }
    }
}
