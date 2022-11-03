const REG_EMAIL = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
const REG_TYPE = {
    EMAIL : "email"
    ,PASSWORD : "password"

}

const validReg = (type,value) => {
    let result = false;
    switch (type) {
        case REG_TYPE.EMAIL :
            result = REG_EMAIL.test(value);
            break;
        default :
            break;
    }
    return result;
}

export {REG_EMAIL,REG_TYPE,validReg}
