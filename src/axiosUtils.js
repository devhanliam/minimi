import axios from "axios";

axios.defaults.withCredentials = true;
export const CONTENT_MULTIPART = 'multipart/form-data'
export const CONTENT_JSON = 'application/json; charset=utf-8'
export const IMG_SRC_HOST = window.location.hostname === 'localhost' ? 'http://localhost:8080' : 'http://124.58.165.95:8080';
const instance = axios.create({
    baseURL: "/api/v1/re-issue"
});
instance.interceptors.request.use((config)=>{
    config.headers["Content-Type"] = "application/json; charset=utf-8";
    config.headers["Authorization"] = localStorage.getItem("accessToken") ? `Bearer ${localStorage.getItem("accessToken")}` : '';
    config.method = "post"
        return config;
    },
    (error) =>{
        return Promise.reject(error);
    }
)
instance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if(error.response.data.errorCode === "EXPIRED_TOKEN"){
            localStorage.clear();
        }
    }
)

export const callApi = (url,method, data,contentType) => {
    contentType = contentType?contentType:"application/json; charset=utf-8"
    return axios({
            url: url,
            method: method,
            data : data,
            headers : {
                'Content-type': contentType,
                'Authorization': localStorage.getItem("accessToken") ? `Bearer ${localStorage.getItem("accessToken")}` : '',
                // 'Accept' : "application/json, text/plain, */*"
            }
        });
}

export default instance;

