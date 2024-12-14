#include <algorithm>
#include <ranges>

namespace ranges = std::ranges;

template<ranges::viewable_range Range, typename Comp> 
void selectionSort(Range& range, Comp comp) {
    auto end = std::end(range);
    for (auto it = std::begin(range); it != end; it++) {
        auto min = ranges::min_element(ranges::subrange(it, end), comp);
        if (min != it) {
            std::iter_swap(min, it);
        }
    }
}