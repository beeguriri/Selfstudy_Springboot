import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import FileUpload from './FileUpload';

const LoginSuccess = () => {

    // const navigate = useNavigate();

    // const onClickLogout = () => {
    //     alert("로그아웃 합니다.")
    //     sessionStorage.removeItem("isLoggedIn")
    //     navigate("/")
    // }

    const onClickLogout = async () => {
        await axios
            .get("/api/user/logout", {
            })
            .then((response) => {
                console.log(response)
                sessionStorage.removeItem("isLoggedIn")
                window.location.href = "/";
            })
            .catch((error) => {
                console.log(error);
            })
        // sessionStorage.removeItem("isLoggedIn")

    }

    return (
        <div>
            {sessionStorage.getItem("isLoggedIn") ? (
                <div>
                    로그인 성공 시 보이는 화면 <br />
                    {sessionStorage.getItem("isLoggedIn")}
                    <FileUpload />
                    <button
                        type="button"
                        onClick={onClickLogout}
                    >로그아웃</button>
                </div>) : (
                <div>
                    false
                    {window.location.href = "/"}
                </div>
                )
            }
        </div>
    );
}

export default LoginSuccess;