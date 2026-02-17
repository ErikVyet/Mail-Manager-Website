
function searchFocus(input) {
    const fieldset = input.parentElement;
    fieldset.style.borderColor = "rgba(255, 255, 255, 0.4)";
}

function searchBlur(input) {
    const fieldset = input.parentElement;
    fieldset.style.borderColor = "rgba(255, 255, 255, 0.2)";
}

function searchInput(input) {
    const value = input.value;
    const navItems = document.querySelectorAll(".setting-container ul li");
    navItems.forEach(item => {
        const text = item.textContent.toLowerCase();
        if (text.includes(value.toLowerCase())) {
            item.style.display = "flex";
        }
        else {
            item.style.display = "none";
        }
    });
}

function activeNavItem(li, index) {
    const navItems = document.querySelectorAll(".setting-container ul li");
    navItems.forEach(item => {
        item.classList.remove("active-list-item");
    });
    li.classList.add("active-list-item");

    const panels = document.querySelectorAll(".content-panel > div");
    panels.forEach(panel => {
        panel.style.display = "none";
    });
    panels[index].style.display = "flex";
}

function openProfileEditingDialog(fieldset) {
    const legend = fieldset.getElementsByTagName("legend")[0];
    const input = fieldset.getElementsByTagName("input")[0];
    
    const dialog = document.getElementsByClassName("profile-editing-dialog")[0];
    const dialogHeader = dialog.querySelector("h3");
    const dialogInput = dialog.querySelector("input");
    dialogHeader.textContent = "Edit your " + legend.textContent.toLowerCase();
    dialogInput.value = input.value;
    dialogInput.setAttribute("type", input.getAttribute("type"));
    dialogInput.focus();
    dialog.showModal();
}

function closeProfileEditingDialog() {
    const dialog = document.getElementsByClassName("profile-editing-dialog")[0];
    dialog.close();
}

function saveProfileEditingDialog() {
    const dialog = document.getElementsByClassName("profile-editing-dialog")[0];
    dialog.close();
}

function onAppearanceChange() {
    const button = document.getElementById("apply-appearance-button");
    button.removeAttribute("disabled");
}

function onLanguageChange() {
    const button = document.getElementById("apply-language-button");
    button.removeAttribute("disabled");
}

function clearProfilePasswordEditingDialog() {
    const dialog = document.getElementsByClassName("profile-password-editing-dialog")[0];
    const inputs = dialog.querySelectorAll("input");
    inputs.forEach(input => {
        input.value = "";
    });
}

function openProfilePasswordEditingDialog() {
    const dialog = document.getElementsByClassName("profile-password-editing-dialog")[0];
    const phone = dialog.querySelector("input[name='phone']");
    phone.value = document.getElementsByClassName("profile-content")[0].querySelector("input[name='phone']").value;
    dialog.showModal();
}

function closeProfilePasswordEditingDialog() {
    const dialog = document.getElementsByClassName("profile-password-editing-dialog")[0];
    dialog.close();
    clearProfilePasswordEditingDialog();
}

function saveProfilePasswordEditingDialog() {
    const dialog = document.getElementsByClassName("profile-password-editing-dialog")[0];
    dialog.close();
    clearProfilePasswordEditingDialog();
}