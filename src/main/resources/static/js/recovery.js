
function onInputFocus(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw white";
}

function onInputBlur(fieldset) {
    fieldset.style.borderBottom = "solid 0.15vw rgba(255, 255, 255, 0.5)";
}

function clearRecoveryInputs() {
    const recoveryContainer = document.getElementsByClassName("recovery-container")[0];
    const inputs = recoveryContainer.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
    }
}

async function getCode() {
    const vmail = document.getElementById("vmail").value;
    const response = await fetch("/code/getForRecovery", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: vmail
    });
    const data = await response.json();
    if (data.status === "error") {
        document.getElementById("recoveryError").textContent = data.message;
    }
    else {
        alert("Code: " + data.code);
    }
}