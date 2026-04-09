/**
 * Standardized Pagination Metadata
 * Cho /history endpoint
 */

package com.freelancemarketplace.backend.toggl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationMetadata {
    private int page;            // 0-indexed
    private int size;            // Items per page
    private long totalElements;  // Total records
    private int totalPages;      // Total pages
    private boolean hasNext;     // Has next page?
    private boolean hasPrevious; // Has previous page?
}

