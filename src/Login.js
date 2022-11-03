/* global history */
/* global location */
/* global window */

/* eslint no-restricted-globals: ["off"] */
import {
    Avatar,
    Box,
    Button,
    Checkbox, Container, createTheme,
    CssBaseline,
    FormControlLabel,
    Grid,
    Link,
    TextField, ThemeProvider, Typography,
} from "@material-ui/core";
import {LockOutlined} from "@material-ui/icons";
import {useState} from "react";
import {REG_TYPE, validReg} from "./validUtil";
import axios from "axios";
import {callApi} from "./axiosUtils";



const theme = createTheme();

const Login = () => {
    const [email, setEmail] = useState("");
    const [emailValid, setEmailValid] = useState({error: false, msg : "이메일을 입력해 주세요",type : REG_TYPE.EMAIL});

    const validCheck = (e) => {
        let value = e.target.value;
        setEmail(value);
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

    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        let params = {
            email: data.get('email'),
            password: data.get('password'),
        }
        callApi("api/v1/login","post",params)
            .then(function (response) {
                console.log(`Bearer ${response.data.accessToken}`);
                localStorage.setItem("accessToken", response.data.accessToken);
                localStorage.setItem("name", response.data.name);
                localStorage.setItem("nickName", response.data.nickName);
                localStorage.setItem("email", response.data.email);
                axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem("accessToken")}`;
                location.href="/";
            })
            .catch(function (error) {
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    console.log(error);
                    alert(error.response.data.message);
                }
            })
            .then(function () {
                //finally
            });
    };
    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    style={{
                        marginTop: '30%',
                        display : 'flex',
                        flexDirection : 'column',
                        alignItems : 'center'

                    }}
                >
                    <Avatar style={{ main: 1, backgroundColor: 'darkblue' , marginBottom : 10}}>
                        <LockOutlined/>
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        미니미
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="이메일"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            helperText={emailValid.msg}
                            error={emailValid.error}
                            value={email}
                            onChange={validCheck}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="비밀번호"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                        {/*<FormControlLabel*/}
                        {/*    control={<Checkbox value="remember" color="primary" />}*/}
                        {/*    label="Remember me"*/}
                        {/*/>*/}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            sx={{ mt: 3, mb: 2 }}
                            style={{marginTop:3,marginBottom:2}}
                        >
                            로그인
                        </Button>
                        <Grid container>
                            <Grid item xs>
                                <Link href="#" variant="body2">
                                    비밀번호 찾기
                                </Link>
                            </Grid>
                            <Grid item>
                                <Link href="/join" variant="body2">
                                    회원가입
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    )
}

export default Login;