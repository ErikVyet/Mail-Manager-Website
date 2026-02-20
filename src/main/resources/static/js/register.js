
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

function disableGetCodeButton() {
    const getCodeButton = document.getElementById("get-code-button");
    getCodeButton.disabled = true;
    setTimeout(enableGetCodeButton, 60000);
}

function enableGetCodeButton() {
    const getCodeButton = document.getElementById("get-code-button");
    getCodeButton.disabled = false;
}

async function getCode() {
    const phone = document.getElementById("phone").value;
    if (phone.length != 10) {
        alert("Phone number must be 10 digits");
        return;
    }
    if (phone.trim() == "") {
        alert("Phone number cannot be empty");
        return;
    }

    const response = await fetch("/code/get", {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });

    const data = await response.json();
    alert("Code: " + data.code);
    disableGetCodeButton();
}