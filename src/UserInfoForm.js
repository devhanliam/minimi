import {Box, Button, Grid, TextField} from "@material-ui/core";
import {useEffect, useState} from "react";
import {REG_TYPE} from "./validUtil";
import {callApi} from "./axiosUtils";

const UserInfoForm= ()=>{
    const [userInfo, setUserInfo] = useState({});
    const [passwordValid, setPasswordValid] = useState({error: false, msg: "비밀번호를 한번더 입력해주세요", type: REG_TYPE.EMAIL});
    const [duplicationCheck, setDuplicationCheck] = useState(false);
    function handleSubmit(e) {
        e.preventDefault();
    }
    useEffect(()=>{
        callApi("/api/v1/user/info","get")
            .then(res=>{
                console.log(res.data);
                setUserInfo(res.data);
            })
            .catch(error=>{
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    alert(error.response.data.message);
                }
            });

    },[]);

    return (
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{mt:3}}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        variant="standard"
                        id="name"
                        name="name"
                        label="이름"
                        autoComplete="name"
                        autoFocus
                        value={userInfo.name || ''}
                        inputProps={{readOnly:true}}
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
                        value={userInfo.nickName || ''}
                        inputProps={{readOnly:true}}
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
                        value={userInfo.email || ''}
                        inputProps={{readOnly:true}}
                        autoComplete="email"
                        autoFocus
                    />
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
                        inputProps={{ readOnly:true }}
                        value={userInfo.mobile || ''}
                    />
                </Grid>
                {/*<Grid item xs={12}>*/}
                {/*    <TextField*/}
                {/*        margin="normal"*/}
                {/*        required*/}
                {/*        fullWidth*/}
                {/*        id="password"*/}
                {/*        name="password"*/}
                {/*        label="비밀번호"*/}
                {/*        autoComplete="password"*/}
                {/*        autoFocus*/}
                {/*        value={userInfo.password}*/}
                {/*        inputProps={{readOnly:true}}*/}
                {/*        type="password"*/}
                {/*    />*/}
                {/*</Grid>*/}
                {/*<Grid item xs={12}>*/}
                {/*    <TextField*/}
                {/*        margin="normal"*/}
                {/*        required*/}
                {/*        fullWidth*/}
                {/*        id="passwordCheck"*/}
                {/*        label="비밀번호 확인"*/}
                {/*        autoComplete="password"*/}
                {/*        autoFocus*/}
                {/*        type="password"*/}
                {/*        helperText={passwordValid.msg}*/}
                {/*        error={passwordValid.error}*/}
                {/*    />*/}
                {/*</Grid>*/}
            </Grid>
            <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{mt:3,mb:2}}
                color="primary"
            >
                수정하기
            </Button>
        </Box>


    )


}
export default UserInfoForm;