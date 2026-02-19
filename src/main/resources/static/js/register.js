
function onInputFocus(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw white";
}

function onInputBlur(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw rgba(255, 255, 255, 0.5)";
}

function clearRegisterInputs() {
    const registerContainer = document.getElementsByClassName("register-container")[0];
    const inputs = registerContainer.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
    }
}