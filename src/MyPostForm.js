import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import {useEffect, useState} from "react";
import {callApi} from "./axiosUtils";
import {TablePagination} from "@mui/material";

const MyPostForm = ()=>{
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [postList, setPostList] = useState([]);
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };


    useEffect(()=>{
        callApi("/api/v1/user/post/list", "get")
            .then(res => {
                console.log(res);
                setPostList(res.data);
            })
            .catch(error => {
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    console.log(error);
                    alert(error.response.data.message);
                }
            })

    },[])

    return (
        <Paper>
            <TableContainer>
                <Table stickyHeader>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                제목
                            </TableCell>
                            <TableCell>
                                작성일
                            </TableCell>
                            <TableCell>
                               조회수
                            </TableCell>
                            <TableCell>
                                공개여부
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            postList.slice(page * rowsPerPage , page * rowsPerPage + rowsPerPage)
                                .map(p => {
                                    return (
                                        <TableRow hover role="row" key={p.postId} tabIndex={-1}
                                                  onClick={()=>{window.location.href=`/post/${p.postId}`}}
                                                  style={{cursor:"pointer"}}
                                        >
                                            <TableCell>
                                                {p.title}
                                            </TableCell>
                                            <TableCell>
                                                {p.writeTime}
                                            </TableCell>
                                            <TableCell>
                                                {p.views}
                                            </TableCell>
                                            <TableCell>
                                                {p.open}
                                            </TableCell>
                                        </TableRow>
                                    )
                                })
                        }

                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={postList.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}

            />
        </Paper>


    );


}

export default MyPostForm;