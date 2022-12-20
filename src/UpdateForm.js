import React, {useEffect, useState} from "react";
import {callApi, CONTENT_MULTIPART} from "./axiosUtils";
import {Box, Button, Grid, styled, Switch, TextField, Tooltip, Typography} from "@material-ui/core";
import LoadingBackDrop from "./LoadingBackDrop";
import {AddPhotoAlternate, DeleteForever} from "@material-ui/icons";
import {Editor} from "@toast-ui/react-editor";
import {useParams} from "react-router-dom";

const UpdateForm = ()=>{

    const [imgSrc,setImgSrc] = useState('');
    // const [previewFlag, setPreviewFlag] = useState(false);
    const [backDropFlag, setBackDropFlag] = useState(false);
    const textRef = React.createRef();
    const { id } = useParams();
    useEffect(() => {
        callApi(`/api/v1/post/info/${id}`,'get')
            .then(response => {
                let img = response.data.file.length > 0 ? `/api/v1/post/image/${response.data.file[0].fileName}` : "";
                setImgSrc(img);
                document.querySelector("#title").value=response.data.title;
                textRef.current.getInstance().setHTML(response.data.content);
                document.querySelector("#openFlag").checked = response.data.openFlag;
            })
            .catch(err => {
                // alert("게시물 불러오는데 실패했습니다");
                console.log(err);
            })
    },[]);
    // const setImagePreview = (file) => {
    //     const reader = new FileReader();
    //     reader.readAsDataURL(file);
    //     return new Promise((resolve)=>{
    //         reader.onload = () => {
    //             setImgSrc(reader.result);
    //             setPreviewFlag(true);
    //             resolve();
    //         }
    //     });
    // }
    // const deleteImage = () => {
    //     document.querySelector("#file").value = '';
    //     setPreviewFlag(false);
    // }
    const submitContent = () => {
        let formData = new FormData();
        // formData.append("file",document.querySelector("#file").files[0])
        let data = {
            "id" : id,
            "title" : document.querySelector("#title").value,
            "content" : textRef.current.getInstance().getHTML(),
            "openFlag" : document.querySelector("#openFlag").checked,
        }
        formData.append("data",
            new Blob([JSON.stringify(data)], {type : "application/json"}));
        // formData.append("title",document.querySelector("#title").value)
        // formData.append("content",textRef.current.getInstance().getHTML());
        setBackDropFlag(true);
        callApi("/api/v1/user/post/update","post",formData,CONTENT_MULTIPART)
            .then((response)=>{
                alert("글이 등록되었습니다");
                window.location.href = `/post/${id}`;
            })
            .catch((error)=>{
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    alert(error.response.data.message);
                }
            }).then(()=> {
            setBackDropFlag(false);
        })

    }
    const AntSwitch = styled(Switch)(({ theme }) => ({
        width: 28,
        height: 16,
        padding: 0,
        display: 'flex',
        '&:active': {
            '& .MuiSwitch-thumb': {
                width: 15,
            },
            '& .MuiSwitch-switchBase.Mui-checked': {
                transform: 'translateX(9px)',
            },
        },
        '& .MuiSwitch-switchBase': {
            padding: 2,
            '&.Mui-checked': {
                transform: 'translateX(12px)',
                color: '#fff',
                '& + .MuiSwitch-track': {
                    opacity: 1,
                    backgroundColor: theme.palette.mode === 'dark' ? '#177ddc' : '#1890ff',
                },
            },
        },
        '& .MuiSwitch-thumb': {
            boxShadow: '0 2px 4px 0 rgb(0 35 11 / 20%)',
            width: 12,
            height: 12,
            borderRadius: 6,
            transition: theme.transitions.create(['width'], {
                duration: 200,
            }),
        },
        '& .MuiSwitch-track': {
            borderRadius: 16 / 2,
            opacity: 1,
            backgroundColor:
                theme.palette.mode === 'dark' ? 'rgba(255,255,255,.35)' : 'rgba(0,0,0,.25)',
            boxSizing: 'border-box',
        },
    }));
    return (
        <Box>
            <LoadingBackDrop open={backDropFlag}/>
            <Box
                style={{display : 'flex',
                    flexDirection : 'column',
                    alignItems : 'center'}}
            >
                <Box
                    component="img"
                    // sx={{
                    //     height: 233,
                    //     width: 350,
                    //     maxHeight: { xs: 233, md: 167 },
                    //     maxWidth: { xs: 350, md: 250 },
                    // }}
                    style={{
                        height: 'auto',
                        width: '70%',
                        // maxHeight: { xs: 233, md: 167 },
                        // maxWidth: { xs: 350, md: 250 },
                    }}
                    src={imgSrc}
                    alt='미리보기'
                    // hidden={!previewFlag}
                />
            </Box>
            {/*<Tooltip title="사진추가" placement="top">*/}
            {/*    <Button*/}
            {/*        variant="contained"*/}
            {/*        component="label"*/}
            {/*        color="primary"*/}
            {/*    >*/}
            {/*        <AddPhotoAlternate/>*/}
            {/*        <input*/}
            {/*            accept="image/*"*/}
            {/*            type="file"*/}
            {/*            hidden*/}
            {/*            id="file"*/}
            {/*            onChange={(e) => {setImagePreview(e.target.files[0])}}*/}
            {/*        />*/}
            {/*    </Button>*/}
            {/*</Tooltip>*/}
            {/*{*/}
            {/*    previewFlag ?*/}
            {/*        <Tooltip title="사진삭제" placement="top">*/}
            {/*            <Button*/}
            {/*                variant="contained"*/}
            {/*                component="label"*/}
            {/*                color="secondary"*/}
            {/*                style={{*/}
            {/*                    marginLeft : 5*/}
            {/*                }}*/}
            {/*                onClick={deleteImage}*/}
            {/*            >*/}
            {/*                <DeleteForever/>*/}
            {/*            </Button>*/}
            {/*        </Tooltip>*/}
            {/*        : null*/}
            {/*}*/}
            <TextField fullWidth
                       variant="outlined"
                       label="제목"
                       id="title"
                       name="title"
                       style={{marginTop:10, marginBottom:10}}/>
            <Editor
                ref={textRef}
                placeholder="인상깊은 하루를 기록하세요."
                height="300px"
                initialEditType="wysiwyg"
                toolbarItems={[
                    ['heading', 'bold', 'italic', 'strike','hr', 'quote'],

                ]}
                initialValue=" "
                useCommandShortcut={false}
            ></Editor>
            <Grid container>
                <Grid item>
                    <Typography style={{padding: 8}}>비공개</Typography>
                </Grid>
                <Grid item style={{marginTop:11}}>
                    <AntSwitch defaultChecked
                               id="openFlag"
                               inputProps={{ 'aria-label': 'ant design'
                               }} />
                </Grid>
                <Grid item>
                    <Typography style={{padding: 8}}>전체공개</Typography>
                </Grid>
            </Grid>
            <Box
                style={{
                    marginTop: 10,
                    marginBottom: 10,
                    display : 'flex',
                    flexDirection : 'column',
                    alignItems : 'center',
                }}
            >
                <Button
                    color="primary"
                    variant="contained"
                    onClick={submitContent}
                    // fullWidth
                    style={{width: '30%'}}
                >등록하기</Button>
            </Box>
        </Box>
    );
}
export default UpdateForm;