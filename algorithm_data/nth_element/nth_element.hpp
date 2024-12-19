#include <algorithm>
#include <ranges>

namespace detail {
    template<typename It>
    auto nthElementImpl(It subrangeBegin, It subrangeEnd, It n) {
        auto pivot = *(subrangeBegin + (std::distance(subrangeBegin, subrangeEnd) / 2));

        auto mid1 = std::partition(subrangeBegin, subrangeEnd, [&](const auto& em) {
            return em < pivot;
        });
        auto mid2 = std::partition(mid1, subrangeEnd, [&](const auto& em) {
            return pivot == em;
        });
       
        if (mid1 == n) {
            return mid1;
        } else if (mid1 < n) {
            return nthElementImpl(std::next(mid1), subrangeEnd, n);
        } else {
            return nthElementImpl(subrangeBegin, mid1, n);
        }
    }
}

template<std::ranges::viewable_range R>
auto nthElement(R& r, size_t n) {
    return detail::nthElementImpl(std::begin(r), std::end(r), std::begin(r) + n);
}
