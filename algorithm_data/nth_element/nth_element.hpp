//DOES NOT WORK YET DO NOT UNCOMMENT

// #include <algorithm>
// #include <ranges>

// namespace ranges = std::ranges;

// template<typename It>
// auto nthElement(It begin, It end, size_t n) {
//     auto pivotIt = std::next(begin, std::distance(begin, end) / 2);
    
//     auto partitionPoint = ranges::partition(begin, end, [&](const auto& em) {
//         return em < *pivotIt;
//     });
//     if (partitionPoint == pivotIt) {
//         return partitionPoint;
//     } else if (pivotIt < partitionPoint) {
//         return nthElement(begin, partitionPoint, n);
//     } else {
//         return nthElement(partitionPoint, end, n);
//     }
// }
