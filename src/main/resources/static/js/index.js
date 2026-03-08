var activeNavIndex = 0;
var navIsExpanded = true;
var navIsSyncing = false;
let selectedMails = [];
let attachedFiles = [];
let attachedImages = [];

window.onload = async function() {
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

    try {
        const response = await fetch("/folder/active", {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        });
        const data = await response.json();

        const activeFolderId = data.activeFolderId != null ? Number.parseInt(data.activeFolderId) : this.document.getElementById('inbox').value;
        activeNavIndex = data.activeNavIndex != null ? Number.parseInt(data.activeNavIndex) : 0;

        navItemActive(activeNavIndex, activeFolderId);
    } 
    catch (error) {
        console.log(error);
    }
}

function base64ToBytes(base64) {
    const binary = atob(base64);
    const bytes = new Uint8Array(binary.length);
    for (let i = 0; i < binary.length; i++) {
        bytes[i] = binary.charCodeAt(i);
    }
    return bytes;
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

async function navItemActive(index, folderId) {
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


    var response;
    var data;

    switch (index) {
        case 1: {
            response = await fetch("/mail/starred", {
                method: "GET",
                headers: { "Content-Type" : "application/json" }
            });
            break;
        }
        case 4: {
            response = await fetch("/mail/all", {
                method: "GET",
                headers: { "Content-Type" : "application/json" }
            });
            break;
        }
        default: {
            response = await fetch("/mail/folder", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ 
                    activeNavIndex: index,
                    activeFolderId: folderId
                })
            });
            break;
        }
    }

    data = await response.json();
    const table = document.getElementsByClassName('mail-list-table')[0];
    const tbody = table.getElementsByTagName('tbody')[0];
    tbody.innerHTML = '';

    for (const [key, value] of Object.entries(data)) {
        const tr = document.createElement('tr');
        tr.className = value.seen ? '' : 'unread-mail';
        tr.onmouseover = () => onTableRowMouseOver(tr);
        tr.onmouseout = () => onTableRowMouseOut(tr);

        var td = document.createElement('td');
        const input = document.createElement('input');
        input.type = 'checkbox';
        input.name = 'selected-mail';
        input.className = 'select-checkbox';
        input.onchange = () => onSelectCheckboxChange(input.checked, tr);
        td.appendChild(input);

        const button = document.createElement('button');
        button.className = 'starred-button';
        button.onclick = () => changeStarredStatus(button);

        const starred = document.createElement('input');
        starred.type = 'hidden';
        starred.value = value.starred;
        starred.name = 'starred';

        const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
        svg.setAttribute('width', '1.4vw');
        svg.setAttribute('viewBox', '0 0 200 200');

        const polygon = document.createElementNS("http://www.w3.org/2000/svg", "polygon");
        polygon.setAttribute('points', '100,10 120,75 190,75 135,115 155,180 100,140 45,180 65,115 10,75 80,75');
        polygon.setAttribute('class', value.starred ? 'starred-icon' : 'unstarred-icon');

        const seen = document.createElement('input');
        seen.type = 'hidden';
        seen.value = value.seen;
        seen.name = 'seen';

        const id = document.createElement('input');
        id.type = 'hidden';
        id.value = value.id;
        id.name = 'id';

        svg.appendChild(polygon);
        button.appendChild(starred);
        button.appendChild(svg);
        td.appendChild(button);
        td.appendChild(seen);
        td.appendChild(id);
        tr.appendChild(td);

        td = document.createElement('td');
        td.textContent = value.email.sender.username;
        td.onclick = () => showMailDetail(Number.parseInt(key));
        tr.appendChild(td);

        td = document.createElement('td');
        td.textContent = value.email.body;
        td.onclick = () => showMailDetail(Number.parseInt(key));
        tr.appendChild(td);

        const date = new Date(value.received);
        td = document.createElement('td');
        td.textContent = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
        td.onclick = () => showMailDetail(Number.parseInt(key));
        tr.appendChild(td);

        tbody.appendChild(tr);
    }

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

async function createNewLabel() {
    const labelName = document.getElementsByName('new-label-name')[0].value;
    const index = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0].getElementsByTagName('li').length;
    if (index == 15) {
        alert('You have reached the maximum number of labels.');
        closeCreateNewLabelDialog();
        return;
    }

    const response = await fetch("/folder/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
            name: labelName,
            system: false,
            created: (new Date()).toISOString()
        })
    });

    const data = await response.json();
    if (data.error != null) {
        alert(data.error);
        closeCreateNewLabelDialog();
        return;
    }
    const folderId = data.folderId;

    const navIconImg = document.createElement('img');
    navIconImg.src = '/image/label.png';
    const navIconButton = document.createElement('button');
    navIconButton.title = labelName;
    navIconButton.onclick = () => navItemActive(index, folderId);
    navIconButton.onmouseover = () => navItemHover(index);
    navIconButton.onmouseout = () => navItemOut(index);
    navIconButton.appendChild(navIconImg);
    const navIconInput = document.createElement('input');
    navIconInput.type = 'hidden';
    navIconInput.value = folderId;
    const navIconItem = document.createElement('li');
    navIconItem.appendChild(navIconButton);
    navIconItem.appendChild(navIconInput);
    navIconItem.onclick = () => navItemActive(index, folderId);
    navIconItem.onmouseover = () => navItemHover(index);
    navIconItem.onmouseout = () => navItemOut(index);
    const navIconList = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0];
    navIconList.appendChild(navIconItem);

    const navLabelSpan = document.createElement('span');
    navLabelSpan.innerText = labelName;
    const navLabelInput = document.createElement('input');
    navLabelInput.type = 'hidden';
    navLabelInput.value = folderId;
    const navLabelItem = document.createElement('li');
    navLabelItem.appendChild(navLabelSpan);
    navLabelItem.appendChild(navLabelInput);
    navLabelItem.onclick = () => navItemActive(index, folderId);
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
    const navLabelListItems = navLabelList.getElementsByTagName('li');
    for (let i = 8; i < navLabelListItems.length; i++) {
        const option = document.createElement('option');
        option.value = navLabelListItems[i].getElementsByTagName('input')[0].value;
        option.innerText = navLabelListItems[i].getElementsByTagName('span')[0].innerText;
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

async function removeExistedLabel() {
    const dialog = document.getElementsByClassName('remove-existed-label-dialog')[0];
    const select = dialog.getElementsByTagName('select')[0];
    const labelToRemove = select.value;

    const navIconList = document.getElementsByClassName('nav-icon')[0].getElementsByTagName('ul')[0];
    const navLabelList = document.getElementsByClassName('nav-label')[0].getElementsByTagName('ul')[0];
    const length = navLabelList.getElementsByTagName('li').length;

    var flag = false;
    for (let i = 8; i < length; i++) {
        if (flag) {
            const newIndex = i - 1;
            const navIconItem = navIconList.getElementsByTagName('li')[newIndex];
            const folderId = navIconItem.getElementsByTagName('input')[0].value;

            navIconItem.getElementsByTagName('button')[0].onclick = () => navItemActive(newIndex, folderId);
            navIconItem.getElementsByTagName('button')[0].onmouseover = () => navItemHover(newIndex);
            navIconItem.getElementsByTagName('button')[0].onmouseout = () => navItemOut(newIndex);

            const navLabelItem = navLabelList.getElementsByTagName('li')[newIndex];
            navLabelItem.onclick = () => navItemActive(newIndex, folderId);
            navLabelItem.onmouseover = () => navItemHover(newIndex);
            navLabelItem.onmouseout = () => navItemOut(newIndex);
        }
        else if (navLabelList.getElementsByTagName('li')[i].getElementsByTagName('input')[0].value === labelToRemove) {
            try {
                const response = await fetch('/folder/delete', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: labelToRemove })
                });

                const data = await response.json();
                if (data.error != null) {
                    alert(data.error);
                    return;
                }

                navIconList.removeChild(navIconList.getElementsByTagName('li')[i]);
                navLabelList.removeChild(navLabelList.getElementsByTagName('li')[i]);
                if (i === activeNavIndex) {
                    activeNavIndex = 0;
                }
                else if (i < activeNavIndex) {
                    activeNavIndex -= 1;
                }

                navItemActive(activeNavIndex, navIconList.getElementsByTagName('li')[0].getElementsByTagName('input')[0].value);
            } 
            catch (error) {
                console.error('Error:', error);
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
            const inputs = document.getElementsByName('seen');
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
            const inputs = document.getElementsByName('seen');
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

async function closeDraftEmailDialog(option) {
    const dialog = document.getElementsByClassName('draft-email-dialog')[0];
    const fields = dialog.getElementsByClassName('draft-email-dialog-field');
    if (option == 'save') {
        const id = document.getElementsByName('draft-id')[0].value;
        const subject = document.getElementsByName('draft-subject')[0].value;
        const body = document.getElementsByName('draft-body')[0].innerHTML;

        const data = new FormData();
        data.append('mailId', id);
        data.append('subject', subject);
        data.append('body', body);
        for (let i = 0; i < attachedFiles.length; i++) {
            data.append('attachments', attachedFiles[i]);
        }
        for (let j = 0; j < attachedImages.length; j++) {
            data.append('attachments', attachedImages[j]);
        }

        const response = await fetch('/mail/save', {
            method: 'POST',
            body: data
        });
        const result = await response.json();
        if (!result.success) {
            alert('Failed to save draft email');
            return;
        }
    }
    else if (option == 'delete') {
        const id = document.getElementsByName('draft-id')[0].value;
        const response = await fetch('/mail/delete', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mailId: id })
        });
        const result = await response.json();
        if (!result.success) {
            alert('Failed to delete draft email');
            return;
        }
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
    navItemActive(activeNavIndex, document.getElementsByName('draft-folder-id')[0].value);
}

//
async function sendDraftEmail() {
    const id = document.getElementsByName('draft-id')[0].value;
    const to = document.getElementsByName('to')[0].value;
    const cc = document.getElementsByName('cc')[0].value;
    const bcc = document.getElementsByName('bcc')[0].value;
    const subject = document.getElementsByName('draft-subject')[0].value;
    const body = document.getElementsByName('draft-body')[0].innerHTML;

    const data = new FormData();
    data.append('mailId', id);
    data.append('to', to);
    data.append('cc', cc);
    data.append('bcc', bcc);
    data.append('subject', subject);
    data.append('body', body);
    for (let i = 0; i < attachedFiles.length; i++) {
        data.append('attachments', attachedFiles[i]);
    }
    for (let j = 0; j < attachedImages.length; j++) {
        data.append('attachments', attachedImages[j]);
    }

    const response = await fetch('/mail/send', {
        method: 'POST',
        body: data
    });
    const result = await response.json();
    if (!result.success) {
        alert('Failed to send draft email');
        return;
    }
    closeDraftEmailDialog(null);
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
        fileElement.onclick = async () => {
            const blob = new Blob([await file.bytes()], { type: file.type });
            const url = URL.createObjectURL(blob);
            window.open(url);
            URL.revokeObjectURL(url);
        };
        draftBodyAttachedFiles.appendChild(fileElement);
    }
    draftBody.style.height = '68%';
    draftBodyAttachedFiles.style.display = 'flex';
    input.value = '';
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
        fileElement.onclick = async () => {
            const blob = new Blob([await file.bytes()], { type: file.type });
            const url = URL.createObjectURL(blob);
            window.open(url);
            URL.revokeObjectURL(url);
        };
        draftBodyAttachedFiles.appendChild(fileElement);
    }
    draftBody.style.height = '68%';
    draftBodyAttachedFiles.style.display = 'flex';
    input.value = '';
}

async function removeAttachment(button, file) {
    event.stopPropagation();
    const fileElement = button.parentElement;
    const draftBody = document.getElementsByName('draft-body')[0];
    const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
    if (file == null) {
        const input = button.querySelector('input');
        const attachmentId = input.value;
        const response = await fetch('/attachment/delete', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ attachmentId: attachmentId })
        });
        const data = await response.json();
        if (!data.success) {
            alert('Something went wrong!');
            return;
        }
    }
    else if (file.type.startsWith('image/')) {
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

async function showMailDetail(mailId) {
    try {
        var response = await fetch("/mail/", {
            method: "POST",
            headers: { "Content-Type": "application/json"},
            body: JSON.stringify({ mailId: mailId })
        });
        var data = await response.json();
        if (!data.success) {
            alert("Something went wrong!");
            return;
        }
        const mail = data.mail;
        
        response = await fetch("/attachment/", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mailId: mailId })
        });
        data = await response.json();
        if (!data.success) {
            alert("Something went wrong!");
            return;
        }
        const attachments = data.attachments;
        const date = new Date(mail.sent);

        if (activeNavIndex == 3) {
            document.getElementsByName('draft-id')[0].value = mail.id;
            document.getElementsByName('draft-subject')[0].value = mail.subject;
            document.getElementsByName('draft-body')[0].innerHTML = mail.body;
            if (attachments.length > 0) {
                const draftBodyAttachedFiles = document.getElementsByClassName('draft-body-attached-files')[0];
                attachments.forEach(attachment => {
                    const div = document.createElement('div');
                    const button = document.createElement('button');
                    const extension = attachment.mime.split('/').pop();
                    const input = document.createElement('input');
                    input.type = "hidden";
                    input.value = attachment.id;
                    div.appendChild(input);
                    if (extension.includes("jpg") || extension.includes("png") || extension.includes("jpeg") || extension.includes("gif")) {
                        div.className = "attached-image";
                        div.title = attachment.name;
                        button.title = "Remove attached image";
                    }
                    else {
                        div.className = "attached-file";
                        div.title = attachment.name;
                        button.title = "Remove attached file";
                    }
                    button.appendChild(input);
                    button.onclick = () => removeAttachment(button, null);
                    div.appendChild(button);
                    div.onclick = () => {
                        const bytes = base64ToBytes(attachment.data);
                        const blob = new Blob([bytes], {type: attachment.mime});
                        const url = URL.createObjectURL(blob);
                        window.open(url);
                        URL.revokeObjectURL(url);
                    };
                    document.getElementsByName('draft-body')[0].style.height = '68%';
                    draftBodyAttachedFiles.appendChild(div);
                });
                draftBodyAttachedFiles.style.display = 'flex';
            }
            
            openDraftEmailDialog();
        }
        else {
            document.getElementsByClassName('mail-content-detail-title')[0].textContent = mail.subject;
            document.getElementsByClassName('mail-content-detail-sender-username')[0].textContent = mail.sender.username;
            document.getElementsByClassName('mail-content-detail-sender-vmail')[0].textContent = "<" + mail.sender.vmail + ">";
            document.getElementsByClassName('mail-content-detail-date')[0].textContent = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
            document.getElementsByClassName('mail-content-detail-body')[0].innerHTML = mail.body;

            const footer = document.getElementsByClassName('mail-content-detail-footer')[0];
            attachments.forEach(attachment => {
                const div = document.createElement('div');
                const extension = attachment.mime.split('/').pop();
                if (extension.includes("jpg") || extension.includes("png") || extension.includes("jpeg") || extension.includes("gif")) {
                    div.className = "mail-content-detail-attached-image";
                    const input = document.createElement('input');
                    input.type = "image";
                    input.setAttribute("hidden", "hidden");
                    div.appendChild(input);
                }
                else {
                    div.className = "mail-content-detail-attached-file";
                    const input = document.createElement('input');
                    input.type = "file";
                    input.setAttribute("hidden", "hidden");
                    div.appendChild(input);
                }
                footer.appendChild(div);
            });

            const mailListContainer = document.getElementsByClassName('mail-list')[0];
            const mailDetailContainer = document.getElementsByClassName('mail-content')[0];
            mailListContainer.style.display = 'none';
            mailDetailContainer.style.display = 'block';
        }
    } 
    catch (error) {
        alert("Something went wrong!");
        return;
    }
}

function showMailList() {
    const mailListContainer = document.getElementsByClassName('mail-list')[0];
    const mailDetailContainer = document.getElementsByClassName('mail-content')[0];
    mailListContainer.style.display = 'block';
    mailDetailContainer.style.display = 'none';
}