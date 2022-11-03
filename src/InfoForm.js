import {Box, Button, Grid, IconButton, TextField, Tooltip, Typography} from "@material-ui/core";
import {Viewer} from "@toast-ui/react-editor";
import React, {Fragment} from "react";
import Comment from "./Comment";
import {Delete, EditSharp} from "@material-ui/icons";
import {SpeedDial, SpeedDialAction, SpeedDialIcon} from '@mui/material';
import {callApi} from "./axiosUtils";
const InfoForm = (props) => {
    const {postInfo} = props;
    const moveUpdate = () => {
        window.location.href=`/post/update/${props.id}`;
    }
    const deletePost = () =>{
        if(window.confirm("삭제 하시겠습니까?")){
            callApi(`/api/v1/user/post/delete/${props.id}`,'post')
                .then(res=>{
                    alert("삭제 되었습니다");
                })
                .catch(err => {
                    alert("삭제 실패했습니다");
                    console.log(err);
                });
        }
    }
    const actions = [
        { icon: <EditSharp color="primary" onClick={moveUpdate}/>, name: '수정' },
        { icon: <Delete color="secondary" onClick={deletePost}/>, name: '삭제' },

    ];

    return (
        <Box>
            <TextField
                inputProps={{
                    readOnly:true
                }}
                label="작성자"
                style={{marginTop:10, marginBottom:10}}
                value={postInfo.writer || ''}
            />
            <TextField
                inputProps={{
                    readOnly:true
                }}
                label="작성일"
                style={{marginTop:10, marginBottom:10,marginLeft:10}}
                value={postInfo.writeTime || ''}
            />
            <TextField
                inputProps={{
                    readOnly:true
                }}
                fullWidth
                label="제목"
                id="title" name="title"
                style={{marginTop:10, marginBottom:10}}
                value={postInfo.title || ''}
            />

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
                    src={postInfo.file}
                    onError={(event)=>{
                        event.target.style.display='none';}}
                    alt='미리보기'
                />
            </Box>
            <Box>
                {/*<Viewer*/}
                {/*    initialValue={postInfo.content}*/}
                {/*/>*/}
                <Typography variant="body1"
                            dangerouslySetInnerHTML={{__html:postInfo.content}}
                            align="center"
                />
            </Box>
            {props.editFlag ?
                <Box style={{ transform: 'translateZ(0px)', flexGrow: 1 }}>
                    <SpeedDial
                        ariaLabel="SpeedDial basic example"
                        style={{ position: 'absolute', bottom: 16, right: 16 }}
                        icon={<SpeedDialIcon/>}
                    >
                        {actions.map((action) => (
                            <SpeedDialAction
                                key={action.name}
                                icon={action.icon}
                                tooltipTitle={action.name}
                            />
                        ))}
                    </SpeedDial>
                </Box>
                :
                null
            }
        </Box>
    )
}
export default InfoForm;