// import { Link } from "react-router-dom";
import axios from "axios";

const LoginSuccess = () => {

    const onClickLogout = async () => {
        sessionStorage.removeItem("user_id")
        await axios
            .get("http://localhost:8080/api/logout", {

            })
            .then((res) => {
                console.log(res)
                sessionStorage.removeItem("user_id")
            })
    }

    return(
        <>
            로그인 성공 시 들어오는 페이지
            <button
                type="button"
            onClick={onClickLogout}
            >로그아웃</button>
        </>
    );
}

export default LoginSuccess;