package ru.mixail_akulov.navcomponent.ui.screens.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.mixail_akulov.navcomponent.R
import ru.mixail_akulov.navcomponent.ui.screens.edit.EditItemViewModel.ScreenState
import ru.mixail_akulov.navcomponent.ui.components.ItemDetails
import ru.mixail_akulov.navcomponent.ui.components.ItemDetailsState
import ru.mixail_akulov.navcomponent.ui.screens.actions.ActionScreen

@Composable
fun EditItemScreen(index: Int) {
    val viewModel = hiltViewModel<EditItemViewModel, EditItemViewModel.Factory> { factory ->
        factory.create(index)
    }
    ActionScreen(
        delegate = viewModel,
        content = { (screenState, onExecuteAction) ->
            SuccessEditItemContent(screenState, onExecuteAction)
        }
    )
}

@Composable
private fun SuccessEditItemContent(
    state: ScreenState,
    onEditButtonClicked: (String) -> Unit
) {
    ItemDetails(
        state = ItemDetailsState(
            loadedItem = state.loadedItem,
            textFieldPlaceholder = stringResource(id = R.string.enter_new_item),
            actionButtonText = stringResource(id = R.string.add),
            isActionInProgress = state.isEditInProgress
        ),
        onActionButtonClicked = onEditButtonClicked,
        modifier = Modifier.fillMaxSize()
    )
}