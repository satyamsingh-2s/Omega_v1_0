# Implementation Plan - UI Enhancements: Level 1 Headings & Scrollable Breadcrumbs

This plan focuses on making Level 1 nodes behave like headings and ensuring the breadcrumb displays all levels (L1+) in a horizontally scrollable container.

## Proposed Changes

### [app]

#### [MODIFY] [UnplannedProjectScreen.kt](file:///P:/omega_v1_0/app/src/main/java/com/example/omega_v1_0/ui/screens/UnplannedProjectScreen.kt)

- **Breadcrumb Updates**:
    - Modify `SingleBranchBreadcrumb` to include `Modifier.horizontalScroll(rememberScrollState())`.
    - Update the logic to include *all* levels from the expansion path (L1, L2, L3...) in the breadcrumb, excluding only the root project itself.
- **Level 1 as Heading**:
    - When a Level 1 node is expanded, style it as a distinctive heading (e.g., using `NEON_GREEN` color and `FontWeight.SemiBold`) instead of a standard row.
    - Ensure it acts as the anchor for the breadcrumb that follows.
- **Compressed Header Refactoring**:
    - Unify `CompressedLevel1Level2Header` and `CompressedBranch` logic to use a single "Path-Aware Header" that renders the full scrollable breadcrumb.
- **Tree Visuals**:
    - Ensure the vertical lines and dots correctly align with the new heading-based structure.

## Verification Plan

### Automated Tests
- Run `analyze_file` to verify no syntax or reference errors.
- Run `gradle_build` to ensure the project compiles.

### Manual Verification
- Render `UnplannedProjectScreenPreview` to check the visual appearance of the new Level 1 headings.
- Verify that drilling deep into sub-tasks (L4, L5) shows a long breadcrumb that can be scrolled horizontally.
- Confirm that clicking any segment in the breadcrumb still navigates correctly.
