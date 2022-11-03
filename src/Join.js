/* global history */
/* global location */
/* global window */

/* eslint no-restricted-globals: ["off"] */
import {
    Avatar,
    Box, Button,
    Container,
    createTheme,
    CssBaseline,
    Grid,
    TextField,
    ThemeProvider, Toolbar,
    Typography
} from "@material-ui/core";
import {LockOutlined} from "@material-ui/icons";
import {useState} from "react";
import {REG_TYPE, validReg} from "./validUtil";
import {callApi} from "./axiosUtils";

const theme = createTheme();
const Join = () => {
    const [email, setEmail] = useState("");
    const [emailValid, setEmailValid] = useState({error: false, msg : "이메일을 입력해 주세요",type : REG_TYPE.EMAIL});
    const [passwordValid, setPasswordValid] = useState({error: false, msg: "비밀번호를 한번더 입력해주세요", type: REG_TYPE.EMAIL});
    const [duplicationCheck, setDuplicationCheck] = useState(false);
    const validCheck = (e) => {
        let value = e.target.value;
        setEmail(value);
        if(duplicationCheck){
            setDuplicationCheck(false);
            setEmailValid({
                error: false
                , msg: ""
                , type : REG_TYPE.EMAIL
            });
        }
        if (validReg(emailValid.type,value )) {

            setEmailValid({
                error: false
                , msg: ""
                , type : REG_TYPE.EMAIL
            });
        } else {
            setEmailValid({
                error: true
                , msg: "이메일 형식이 아닙니다"
                , type : REG_TYPE.EMAIL
            })
        }

    }

    function handleSubmit(e) {
        e.preventDefault();
        const data = new FormData(e.currentTarget);
        let passwordConfirm = document.querySelector("#passwordCheck");

        if(!duplicationCheck){
            alert("이메일 중복확인 후 이용해주세요");
            setEmailValid({
                error: true
                , msg: "이메일을 입력해주세요"
                , type : REG_TYPE.EMAIL
            });
            return;
        }

        if(data.get('password') !== passwordConfirm.value){
            setPasswordValid({error:true,msg: "비밀번호를 똑같이 입력해주세요", type: ""});
            passwordConfirm.focus();
            return;
        }else{
            setPasswordValid({error: false, msg: "", type: ""});
        }

        let params = {
            email: data.get('email'),
            name: data.get('name'),
            nickName: data.get('nickName'),
            mobile: data.get('mobile'),
            password: data.get('password'),
        }

        callApi("/api/v1/join","post",params)
            .then((response)=>{
                alert("회원가입이 완료 되었습니다. 로그인을 진행해주세요");
                localStorage.setItem("email", response.data);
                location.href = "/login";
            })
            .catch((error)=>{
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    alert(error.response.data.message);
                }

            })

    }

    function checkDuplication() {
        let email = document.querySelector("#email");
        if(email.value.replace(" ","") === ""){
            setEmailValid({
                error: true
                , msg: "이메일을 입력해주세요"
                , type : REG_TYPE.EMAIL
            });
            email.focus();
            return;
        }
        let params = {email : email.value};
        callApi("/api/v1/join/duplication-check","post",params)
            .then((response)=>{
                setEmailValid({
                    error: false
                    , msg: "중복확인이 완료되었습니다"
                    , type : REG_TYPE.EMAIL
                });
                setDuplicationCheck(true);

            })
            .catch((error)=>{
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    alert(error.response.data.message);
                }
            })

    }

    return (
        <ThemeProvider theme={theme}>
            <Toolbar/>
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <Box sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                }}
                    style={{
                        marginTop: 10,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center'
                    }}
                >
                    <Typography component="h1" variant='h5'>
                        회원가입
                    </Typography>
                    <Box component="form" noValidate onSubmit={handleSubmit} sx={{mt:3}}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="name"
                                    name="name"
                                    label="이름"
                                    autoComplete="name"
                                    autoFocus
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="nickName"
                                    name="nickName"
                                    label="활동명"
                                    autoComplete="nickName"
                                    autoFocus
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="email"
                                    name="email"
                                    label="이메일"
                                    value={email}
                                    autoComplete="email"
                                    autoFocus
                                    onChange={validCheck}
                                    helperText={emailValid.msg}
                                    error={emailValid.error}
                                />
                                <Button

                                    size="medium"
                                    variant="contained"
                                    sx={{mt:3,mb:2}}
                                    color="primary"
                                    onClick={checkDuplication}
                                >
                                    이메일 중복확인
                                </Button>
                            </Grid>
                            {/*<Grid item xs={3}>*/}
                            {/*    */}
                            {/*   */}
                            {/*</Grid>*/}
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="mobile"
                                    name="mobile"
                                    label="전화번호"
                                    autoComplete="mobile"
                                    autoFocus
                                    onChange={(e) =>{e.target.value=e.target.value
                                        .replace(/[^0-9.]/g, '')
                                        .replace(/(\..*)\./g, '$1')}}
                                    helperText="-없이 숫자만 입력해주세요"
                                    inputProps={{ maxLength: 12 }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="password"
                                    name="password"
                                    label="비밀번호"
                                    autoComplete="password"
                                    autoFocus
                                    type="password"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="passwordCheck"
                                    label="비밀번호 확인"
                                    autoComplete="password"
                                    autoFocus
                                    type="password"
                                    helperText={passwordValid.msg}
                                    error={passwordValid.error}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt:3,mb:2}}
                            color="primary"
                        >
                            가입하기
                        </Button>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );


}

export default Join;