#include <algorithm>
#include <ranges>

namespace ranges = std::ranges;

template<ranges::viewable_range Range, typename Pred>
auto partition(Range& r, Pred pred) {
    auto first = ranges::find_if_not(r, pred);

    //if all elements in the range match the given predicate, we are fully partitioned
    if (first == std::end(r)) {
        return first;
    }

    for (auto it = first + 1; it != std::end(r); it++) {
        if (pred(*it)) {
            std::iter_swap(it, first);
            first++;
        }
    }
    return first;
}