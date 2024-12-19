#include <algorithm>
#include <ranges>

namespace detail {
    template<typename It, typename Pred>
    void quicksortImpl(It first, It last) {
        if (first == last) {
            return;
        }
    
        auto pivotIdx = std::distance(first, last) / 2;
        const auto& pivot = *std::next(first, pivotIdx);

        auto mid1 = std::partition(first, last, [&](const auto& em) {
            return em < pivot;
        });
        auto mid2 = std::partition(mid1, last, [&](const auto& em) {
            return !(pivot < em);
        });
    
        quicksortImpl(first, mid1);
        quicksortImpl(mid2, last);
    }
}

template<std::ranges::viewable_range Range>
void quicksort(Range& range) {
    detail::quicksortImpl(std::begin(range), std::end(range));
}
