

function showRegister() {
    const loginContainer = document.getElementsByClassName("login-container")[0];
    const registerContainer = document.getElementsByClassName("register-container")[0];
    const recoveryContainer = document.getElementsByClassName("recovery-container")[0];

    loginContainer.style.animation = "bring-back 0.8s ease-in-out forwards";
    setTimeout(() => {
        registerContainer.style.animation = "bring-front 0.5s ease-in-out forwards";
        recoveryContainer.style.animation = "bring-middle 0.5s ease-in-out forwards";
    }, 500);
    setTimeout(() => { clearLoginInputs(); }, 1000);
}

function showRecovery() {
    const loginContainer = document.getElementsByClassName("login-container")[0];
    const registerContainer = document.getElementsByClassName("register-container")[0];
    const recoveryContainer = document.getElementsByClassName("recovery-container")[0];

    recoveryContainer.style.animation = "bring-front-from-back 0.8s ease-in-out forwards";
    setTimeout(() => {
        loginContainer.style.animation = "push-middle 0.5s ease-in-out forwards";
        registerContainer.style.animation = "push-back 0.5s ease-in-out forwards";
    }, 500);
    setTimeout(() => { clearLoginInputs(); }, 1000);
}

function showLogin(container) {
    const loginContainer = document.getElementsByClassName("login-container")[0];
    const registerContainer = document.getElementsByClassName("register-container")[0];
    const recoveryContainer = document.getElementsByClassName("recovery-container")[0];

    if (container == registerContainer) {
        loginContainer.style.animation = "bring-front-from-back 0.8s ease-in-out forwards";
        setTimeout(() => {
            registerContainer.style.animation = "push-middle 0.5s ease-in-out forwards";
            recoveryContainer.style.animation = "push-back 0.5s ease-in-out forwards";
        }, 500);
        setTimeout(() => { clearRegisterInputs(); }, 1000);
    }
    else {
        recoveryContainer.style.animation = "bring-back 0.8s ease-in-out forwards";
        setTimeout(() => {
            loginContainer.style.animation = "bring-front 0.5s ease-in-out forwards";
            registerContainer.style.animation = "bring-middle 0.5s ease-in-out forwards";
        }, 500);
        setTimeout(() => { clearRecoveryInputs(); }, 1000);
    }
}

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

function clearRegisterInputs() {
    const registerContainer = document.getElementsByClassName("register-container")[0];
    const inputs = registerContainer.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
    }
}

function clearRecoveryInputs() {
    const recoveryContainer = document.getElementsByClassName("recovery-container")[0];
    const inputs = recoveryContainer.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].value = "";
    }
}