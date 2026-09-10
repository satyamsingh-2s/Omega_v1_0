# Implementation Plan - Fix Checkbox Toggle Logic

The `onToggleCompleted` checkbox in `UnplannedProjectScreen` is not working because the logic in `UnplannedProjectViewModel` is flipped. When the checkbox is checked (`true`), it calls `markIncomplete`, and when unchecked (`false`), it calls `markCompleted`.

## Proposed Changes

### [app]

#### [MODIFY] [UnplannedProjectViewModel.kt](file:///P:/omega_v1_0/app/src/main/java/com/example/omega_v1_0/ui/viewmodel/UnplannedProjectViewModel.kt)

- Correct the logic in `toggleCompleted` to call `markCompleted` when the target state is `true` and `markIncomplete` when the target state is `false`.

```kotlin
    fun toggleCompleted(nodeId: Long, isCompleted: Boolean) {
        if (isCompleted) {
            markCompleted(nodeId)
        } else {
            markIncomplete(nodeId)
        }
    }
```

## Verification Plan

### Automated Tests
- Run `gradle_build` to ensure the project compiles.

### Manual Verification
- Deploy the app to a device.
- Navigate to the "Unplanned Projects" screen.
- Expand a project and find a leaf node.
- Click the checkbox.
- Verify that the checkbox state updates correctly and the node is marked as completed in the UI (progress bar should update).
- Uncheck the checkbox and verify it returns to the incomplete state.
