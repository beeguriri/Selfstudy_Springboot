import { Link } from "react-router-dom";

const LoginSuccess = () => {

    const logoutEvent = () => {
        sessionStorage.removeItem("user_id")
        // setIsLogin(false);
    }

    return(
        <>
            로그인 성공 시 들어오는 페이지
            <ul>
                <li><Link to="/logout" onClick={logoutEvent}>로그아웃</Link></li>
            </ul>
        </>
    );
}

export default LoginSuccess;