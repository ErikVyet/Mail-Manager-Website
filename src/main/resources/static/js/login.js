
function onInputFocus(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw white";
}

function onInputBlur(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw rgba(255, 255, 255, 0.5)";
}

function clearLoginInputs() {
    const loginContainer = document.getElementsByClassName("login-container")[0];
    const inputs = loginContainer.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
    }
}