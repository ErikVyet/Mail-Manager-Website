var activeNavIndex = 0;
var navIsExpanded = true;
var navIsSyncing = false;
let selectedMails = [];
let attachedFiles = [];
let attachedImages = [];

window.onload = function() {
    const navIcon = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[activeNavIndex];
    const navLabel = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[activeNavIndex];
    navIcon.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
    navLabel.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
    navLabel.style.fontWeight = 'bold';

    const pageTitle = document.getElementsByTagName('title')[0];
    pageTitle.innerText = 'Inbox';

    // Adding copy and paste functionality to draft body
    const draftBody = document.getElementsByName('draft-body')[0];
    draftBody.addEventListener("paste", (e) => {
        e.preventDefault();
        const text = e.clipboardData.getData('text/plain');
        document.execCommand("insertText", false, text);
    });
    draftBody.addEventListener("click", (e) => {
        const link = e.target.closest("a");
        if (link && (e.ctrlKey || e.metaKey)) {
            window.open(link.href, "_blank");
        }
    });

    // Load labels from database into navigation bar and move-to dialog
    
}

function expandNav(button) {
    const navLabels = document.getElementsByClassName('nav-label')[0];
    navLabels.style.display = 'block';

    const container = document.getElementsByClassName('container')[0];
    container.style.gridTemplateColumns = '0.6fr 1.4fr 8fr';

    navIsExpanded = true;

    button.onclick = () => collapseNav(button);
}

function collapseNav(button) {
    const navLabels = document.getElementsByClassName('nav-label')[0];
    navLabels.style.display = 'none';

    const container = document.getElementsByClassName('container')[0];
    container.style.gridTemplateColumns = '0.6fr 0fr 9.4fr';

    navIsExpanded = false;

    button.onclick = () => expandNav(button);
}

function navItemHover(index) {
    if (index != activeNavIndex) {
        const navIcon = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
        const navLabel = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
        navIcon.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
        navLabel.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
    }
    if (navIsExpanded != true) {
        expandNav(document.getElementsByClassName('menu-button')[0]);
        navIsExpanded = false;
    }
}

function navItemOut(index) {
    if (index != activeNavIndex) {
        const navIcon = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
        const navLabel = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
        navIcon.style.backgroundColor = '#020409';
        navLabel.style.backgroundColor = '#020409';
    }
    if (navIsExpanded != true) {
        collapseNav(document.getElementsByClassName('menu-button')[0]);
    }
}

function navItemActive(index) {
    const previousNavIcon = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[activeNavIndex];
    const previousNavLabel = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[activeNavIndex];
    previousNavIcon.style.backgroundColor = '#020409';
    previousNavLabel.style.backgroundColor = '#020409';
    previousNavLabel.style.fontWeight = 'normal';

    const currentNavIcon = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
    const currentNavLabel = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0].getElementsByTagName('li')[index];
    currentNavIcon.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
    currentNavLabel.style.backgroundColor = 'rgba(220, 220, 220, 0.1)';
    currentNavLabel.style.fontWeight = 'bold';

    activeNavIndex = index;

    const pageTitle = document.getElementsByTagName('title')[0];
    pageTitle.innerText = currentNavLabel.innerText;

    expandNav(document.getElementsByClassName('menu-button')[0]);
    showMailList();
}

function openCreateNewLabelDialog() {
    const dialog = document.getElementsByClassName('create-new-label-dialog')[0];
    dialog.showModal();
    window.blur();
}

function closeCreateNewLabelDialog() {
    const dialog = document.getElementsByClassName('create-new-label-dialog')[0];
    dialog.close();
    document.getElementsByName('new-label-name')[0].value = '';
    enableCreateNewLabelButton('');
    window.focus();
}

function enableCreateNewLabelButton(text) {
    const createButton = document.getElementsByClassName('create-new-label-dialog-buttons')[0].getElementsByTagName('button')[1];
    if (text.trim() === '') {
        createButton.disabled = true;
    } 
    else {
        createButton.disabled = false;
    }
}

function createNewLabel() {
    const labelName = document.getElementsByName('new-label-name')[0].value;
    const index = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li').length;
    if (index == 15) {
        alert('You have reached the maximum number of labels.');
        closeCreateNewLabelDialog();
        return;
    }

    const navIconImg = document.createElement('img');
    navIconImg.src = './resource/image/label.png';
    const navIconButton = document.createElement('button');
    navIconButton.title = labelName;
    navIconButton.onclick = () => navItemActive(index);
    navIconButton.onmouseover = () => navItemHover(index);
    navIconButton.onmouseout = () => navItemOut(index);
    navIconButton.appendChild(navIconImg);
    const navIconItem = document.createElement('li');
    navIconItem.appendChild(navIconButton);
    const navIconList = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0];
    navIconList.appendChild(navIconItem);

    const navLabelItem = document.createElement('li');
    navLabelItem.innerText = labelName;
    navLabelItem.onclick = () => navItemActive(index);
    navLabelItem.onmouseover = () => navItemHover(index);
    navLabelItem.onmouseout = () => navItemOut(index);
    const navLabelList = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0];
    navLabelList.appendChild(navLabelItem);

    closeCreateNewLabelDialog();
}

function openRemoveExistedLabelDialog() {
    const dialog = document.getElementsByClassName('remove-existed-label-dialog')[0];
    
    const option = document.createElement('option');
    option.value = '';
    option.innerText = '-- Select a label --';
    option.disabled = true;
    option.defaultSelected = true;
    const select = dialog.getElementsByTagName('select')[0];
    select.innerHTML = '';
    select.appendChild(option);

    //Backend integration to get existing labels
    const navLabelList = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0];
    for (let i = 8; i < navLabelList.getElementsByTagName('li').length; i++) {
        const option = document.createElement('option');
        option.value = navLabelList.getElementsByTagName('li')[i].innerText;
        option.innerText = navLabelList.getElementsByTagName('li')[i].innerText;
        select.appendChild(option);
    }

    dialog.showModal();
    window.blur();
}

function closeRemoveExistedLabelDialog() {
    const dialog = document.getElementsByClassName('remove-existed-label-dialog')[0];
    dialog.close();
    const select = dialog.getElementsByTagName('select')[0];
    select.innerHTML = '';
    enableRemoveExistedLabelButton('');
    window.focus();
}

function enableRemoveExistedLabelButton(value) {
    const removeButton = document.getElementsByClassName('remove-existed-label-dialog-buttons')[0].getElementsByTagName('button')[1];
    if (value === '') {
        removeButton.disabled = true;
    } 
    else {
        removeButton.disabled = false;
    }
}

function removeExistedLabel() {
    const dialog = document.getElementsByClassName('remove-existed-label-dialog')[0];
    const select = dialog.getElementsByTagName('select')[0];
    const labelToRemove = select.value;

    const navIconList = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0];
    const navLabelList = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0];
    const length = navLabelList.getElementsByTagName('li').length;

    var flag = false;
    for (let i = 0; i < length; i++) {
        if (flag) {
            const newIndex = i - 1;
            const navIconItem = navIconList.getElementsByTagName('li')[newIndex];
            navIconItem.getElementsByTagName('button')[0].onclick = () => navItemActive(newIndex);
            navIconItem.getElementsByTagName('button')[0].onmouseover = () => navItemHover(newIndex);
            navIconItem.getElementsByTagName('button')[0].onmouseout = () => navItemOut(newIndex);

            const navLabelItem = navLabelList.getElementsByTagName('li')[newIndex];
            navLabelItem.onclick = () => navItemActive(newIndex);
            navLabelItem.onmouseover = () => navItemHover(newIndex);
            navLabelItem.onmouseout = () => navItemOut(newIndex);
        }
        else if (navLabelList.getElementsByTagName('li')[i].innerText === labelToRemove) {
            navIconList.removeChild(navIconList.getElementsByTagName('li')[i]);
            navLabelList.removeChild(navLabelList.getElementsByTagName('li')[i]);
            if (i === activeNavIndex) {
                activeNavIndex = 0;
                navItemActive(0);
            }
            else if (i < activeNavIndex) {
                activeNavIndex -= 1;
            }
            flag = true;
        }
    }

    closeRemoveExistedLabelDialog();
}

function openMoveToDialog() {
    const dialog = document.getElementsByClassName('move-to-dialog')[0];
    dialog.showModal();
}

function closeMoveToDialog() {
    const dialog = document.getElementsByClassName('move-to-dialog')[0];
    dialog.close();
}

function changeStarredStatus(button) {
    const starred = button.getElementsByTagName('input')[0].value;
    const star = button.getElementsByTagName('svg')[0].getElementsByTagName('polygon')[0];
    if (starred === 'false') {
        star.style.fill = 'gold';
        star.style.stroke = 'gold';
        button.getElementsByTagName('input')[0].value = 'true';
    }
    else {
        star.style.fill = 'none';
        star.style.stroke = 'rgba(220, 220, 220, 0.6)';
        button.getElementsByTagName('input')[0].value = 'false';
    }
}

function onTableRowMouseOver(row) {
    const column = row.getElementsByTagName('td')[0];
    column.style.opacity = '1';
}

function onTableRowMouseOut(row) {
    const column = row.getElementsByTagName('td')[0];
    const checkbox = column.getElementsByTagName('input')[0];
    if (!checkbox.checked) {
        column.style.opacity = '0.5';
    }
}

function updateSelectedMails() {
    const checkboxes = document.getElementsByClassName('select-checkbox');
    selectedMails.length = 0;
    for (let i = 0; i < checkboxes.length; i++) {
        const row = checkboxes[i].parentElement.parentElement;
        if (checkboxes[i].checked) {
            selectedMails.push(document.getElementsByName('id')[row.rowIndex].value);
        }
    }
}

function updateSelectAllCheckbox() {
    const checkboxes = document.getElementsByClassName('select-checkbox');
    var numberOfChecked = 0;
    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            numberOfChecked++;
        }
    }
    const selectAllCheckbox = document.getElementsByClassName('select-all-checkbox')[0];
    const mailRefreshControl = document.getElementsByClassName('mail-refresh-control')[0];
    const mailOtherControl = document.getElementsByClassName('mail-other-control')[0];
    if (numberOfChecked == 0) {
        selectAllCheckbox.indeterminate = false;
        selectAllCheckbox.checked = false;
        mailRefreshControl.style.display = 'flex';
        mailOtherControl.style.display = 'none';
    }
    else if (numberOfChecked == checkboxes.length) {
        selectAllCheckbox.indeterminate = false;
        selectAllCheckbox.checked = true;
        mailRefreshControl.style.display = 'none';
        mailOtherControl.style.display = 'flex';
    }
    else {
        selectAllCheckbox.indeterminate = true;
        mailRefreshControl.style.display = 'none';
        mailOtherControl.style.display = 'flex';
    }
}

function onSelectCheckboxChange(state, row) {
    if (state) {
        row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
        selectedMails.push(document.getElementsByName('id')[row.rowIndex].value);
    }
    else {
        row.style.backgroundColor = '#0e1116';
        selectedMails.splice(selectedMails.indexOf(document.getElementsByName('id')[row.rowIndex].value), 1);
    }
    updateSelectedMails();
    updateSelectAllCheckbox();
}

function onSelectAllCheckboxChange(state) {
    const checkboxes = document.getElementsByClassName('select-checkbox');
    for (let i = 0; i < checkboxes.length; i++) {
        const column = checkboxes[i].parentElement;
        const row = column.parentElement;
        if (state) {
            if (!checkboxes[i].checked) {
                checkboxes[i].checked = true;
                column.style.opacity = '1';
                row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
            }
        }
        else if (!state) {
            if (checkboxes[i].checked) {
                checkboxes[i].checked = false;
                column.style.opacity = '0.5';
                row.style.backgroundColor = '#0e1116';
            }
        }
    }
    updateSelectedMails();
    updateSelectAllCheckbox();
}

function performBulkAction(value) {
    selectedMails.length = 0;
    switch (value) {
        case "0": {
            onSelectAllCheckboxChange(true);
            break;
        }
        case "1": {
            onSelectAllCheckboxChange(false);
            break;
        }
        case "2": {
            const inputs = document.getElementsByName('read');
            for (let i = 0; i < inputs.length; i++) {
                const row = inputs[i].parentElement.parentElement;
                const column = inputs[i].parentElement;
                const checkbox = row.getElementsByClassName('select-checkbox')[0];
                if (inputs[i].value == "true") {
                    row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
                    column.style.opacity = '1';
                    checkbox.checked = true;
                }
                else {
                    row.style.backgroundColor = '#0e1116';
                    column.style.opacity = '0.5';
                    checkbox.checked = false;
                }
            }
            updateSelectedMails();
            updateSelectAllCheckbox();
            break;
        }
        case "3": {
            const inputs = document.getElementsByName('read');
            for (let i = 0; i < inputs.length; i++) {
                const row = inputs[i].parentElement.parentElement;
                const column = inputs[i].parentElement;
                const checkbox = row.getElementsByClassName('select-checkbox')[0];
                if (inputs[i].value == "false") {
                    row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
                    column.style.opacity = '1';
                    checkbox.checked = true;
                }
                else {
                    row.style.backgroundColor = '#0e1116';
                    column.style.opacity = '0.5';
                    checkbox.checked = false;
                }
            }
            updateSelectedMails();
            updateSelectAllCheckbox();
            break;
        }
        case "4": {
            const inputs = document.getElementsByName('starred');
            for (let i = 0; i < inputs.length; i++) {
                const column = inputs[i].parentElement.parentElement;
                const row = column.parentElement;
                const checkbox = row.getElementsByClassName('select-checkbox')[0];
                if (inputs[i].value == "true") {
                    row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
                    column.style.opacity = '1';
                    checkbox.checked = true;
                }
                else {
                    row.style.backgroundColor = '#0e1116';
                    column.style.opacity = '0.5';
                    checkbox.checked = false;
                }
            }
            updateSelectedMails();
            updateSelectAllCheckbox();
            break;
        }
        case "5": {
            const inputs = document.getElementsByName('starred');
            for (let i = 0; i < inputs.length; i++) {
                const column = inputs[i].parentElement.parentElement;
                const row = column.parentElement;
                const checkbox = row.getElementsByClassName('select-checkbox')[0];
                if (inputs[i].value == "false") {
                    row.style.backgroundColor = 'rgba(100, 100, 100, 0.1)';
                    column.style.opacity = '1';
                    checkbox.checked = true;
                }
                else {
                    row.style.backgroundColor = '#0e1116';
                    column.style.opacity = '0.5';
                    checkbox.checked = false;
                }
            }
            updateSelectedMails();
            updateSelectAllCheckbox();
            break;
        }
        default:
            break;
    }
}

function openDraftEmailDialog() {
    const dialog = document.getElementsByClassName('draft-email-dialog')[0];
    dialog.showModal();
    window.blur();
}

function closeDraftEmailDialog(option) {
    const dialog = document.getElementsByClassName('draft-email-dialog')[0];
    const fields = dialog.getElementsByClassName('draft-email-dialog-field');
    if (option == 'save') {
        // Backend implementations here
    }
    for (let i = 0; i < fields.length - 1; i++) {
        const input = fields[i].getElementsByTagName('input')[0];
        input.value = '';
        onDraftEmailDialogFieldBlur(input);
    }
    attachedFiles.length = 0; 
    attachedImages.length = 0;
    const draftBody = document.getElementsByName('draft-body')[0];
    const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
    draftBody.innerHTML = '';
    draftBodyAttachedFiles.innerHTML = '';
    draftBody.style.height = '98%';
    draftBodyAttachedFiles.style.display = 'none';
    dialog.close();
    window.focus();
}

function onDraftEmailDialogFieldFocus(input) {
    const label = input.parentElement.getElementsByTagName('label')[0];
    label.style.display = 'block';
    input.style.paddingLeft = '5vw';
    input.onblur = () => onDraftEmailDialogFieldBlur(input);
}

function onDraftEmailDialogFieldBlur(input) {
    const label = input.parentElement.getElementsByTagName('label')[0];
    if (input.value.trim() === '') {
        label.style.display = 'none';
        input.value = '';
        input.style.paddingLeft = '0vw';
    }
}

function openInsertLinkDialog() {
    const dialog = document.getElementsByClassName('insert-link-dialog')[0];
    const linkTextInput = document.getElementsByName('link-text')[0];
    dialog.showModal();
    linkTextInput.focus();
}

function closeInsertLinkDialog() {
    const dialog = document.getElementsByClassName('insert-link-dialog')[0];
    const linkTextInput = document.getElementsByName('link-text')[0];
    const linkUrlInput = document.getElementsByName('link-url')[0];
    linkTextInput.value = '';
    linkUrlInput.value = '';
    onInsertLinkInputChange();
    dialog.close();
}

function onInsertLinkInputChange() {
    const linkTextInput = document.getElementsByName('link-text')[0];
    const linkUrlInput = document.getElementsByName('link-url')[0];
    const insertLinkButton = document.getElementsByClassName('insert-link-button')[0];
    if (linkTextInput.value.trim() === '' || linkUrlInput.value.trim() === '') {
        insertLinkButton.onmouseover = () => null;
        insertLinkButton.onmouseout = () => null;
        insertLinkButton.style.cursor = 'default';
        insertLinkButton.style.opacity = '0.7';
    }
    else {
        insertLinkButton.style.cursor = 'pointer';
        insertLinkButton.style.opacity = '1';
        insertLinkButton.onmouseover = () => {
            insertLinkButton.style.backgroundColor = 'rgba(220, 220, 220, 0.8)';
            insertLinkButton.style.transition = 'background-color 0.15s';
            
        };
        insertLinkButton.onmouseout = () => {
            insertLinkButton.style.backgroundColor = 'white';
        }
        insertLinkButton.onclick = () => {
            const draftBody = document.getElementsByName('draft-body')[0];
            draftBody.innerHTML += `<a href='${linkUrlInput.value}'>${linkTextInput.value}</a>`;
            closeInsertLinkDialog();
        };
    }
}

function onAttachFilesInputChange(input) {
    const length = attachedFiles.push(...input.files);
    const draftBody = document.getElementsByName('draft-body')[0];
    const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
    for (let i = length - input.files.length; i < length; i++) {
        const file = attachedFiles[i];
        const fileElement = document.createElement('div');
        const removeButton = document.createElement('button');
        fileElement.classList.add('attached-file');
        removeButton.title = 'Remove attached file';
        removeButton.onclick = () => removeAttachment(removeButton, file);
        fileElement.title = file.name;
        fileElement.appendChild(removeButton);
        draftBodyAttachedFiles.appendChild(fileElement);
    }
    draftBody.style.height = '68%';
    draftBodyAttachedFiles.style.display = 'flex';
}

function onAttachImagesInputChange(input) {
    const length = attachedImages.push(...input.files);
    const draftBody = document.getElementsByName('draft-body')[0];
    const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
    for (let i = length - input.files.length; i < length; i++) {
        const file = attachedImages[i];
        const fileElement = document.createElement('div');
        const removeButton = document.createElement('button');
        fileElement.classList.add('attached-image');
        removeButton.title = 'Remove attached image';
        removeButton.onclick = () => removeAttachment(removeButton, file);
        fileElement.title = file.name;
        fileElement.appendChild(removeButton);
        draftBodyAttachedFiles.appendChild(fileElement);
    }
    draftBody.style.height = '68%';
    draftBodyAttachedFiles.style.display = 'flex';
}

function removeAttachment(button, file) {
    const fileElement = button.parentElement;
    const draftBody = document.getElementsByName('draft-body')[0];
    const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
    if (file.type.startsWith('image/')) {
        attachedImages.splice(attachedImages.indexOf(file), 1);
    }
    else {
        attachedFiles.splice(attachedFiles.indexOf(file), 1);
    }
    draftBodyAttachedFiles.removeChild(fileElement);
    if (draftBodyAttachedFiles.children.length == 0) {
        draftBody.style.height = '98%';
        draftBodyAttachedFiles.style.display = 'none';
    }
}

function clearMailFilterFields() {
    const dialog = document.getElementsByClassName('mail-filter-dialog')[0];
    dialog.querySelectorAll('input').forEach(input => input.value = '');
    dialog.querySelectorAll('select').forEach(select => select.selectedIndex = 0);
}

function openMailFilterDialog() {
    const dialog = document.getElementsByClassName('mail-filter-dialog')[0];
    const button = document.getElementsByClassName('filter-button')[0];
    dialog.showModal();
    button.setAttribute('hidden', 'hidden');
}

function closeMailFilterDialog() {
    const dialog = document.getElementsByClassName('mail-filter-dialog')[0];
    const button = document.getElementsByClassName('filter-button')[0];
    dialog.close();
    button.removeAttribute('hidden');
    clearMailFilterFields();
}

function showMailDetail(mailId) {
    const mailListContainer = document.getElementsByClassName('mail-list')[0];
    const mailDetailContainer = document.getElementsByClassName('mail-content')[0];
    mailListContainer.style.display = 'none';
    mailDetailContainer.style.display = 'block';
}

function showMailList() {
    const mailListContainer = document.getElementsByClassName('mail-list')[0];
    const mailDetailContainer = document.getElementsByClassName('mail-content')[0];
    mailListContainer.style.display = 'block';
    mailDetailContainer.style.display = 'none';
}