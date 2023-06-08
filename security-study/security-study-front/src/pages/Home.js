import { Link } from "react-router-dom";
import axios from "axios";

const Home = () => {

    const onClickLogout = async () => {
        await axios
            .get("/api/user/logout", {
            })
            .then((response) => {
                console.log(response)
                sessionStorage.removeItem("isLoggedIn")
                sessionStorage.removeItem("id")
                window.location.href = "/";
            })
            .catch((error) => {
                console.log(error);
            })
        // sessionStorage.removeItem("isLoggedIn")

    }

    return (
        <div>
            홈화면 입니다!
            {sessionStorage.getItem("isLoggedIn") ? (
                <div>
                    {sessionStorage.getItem("isLoggedIn")}
                    <form className="loginform" onSubmit={onClickLogout}>
                        <button
                            type="submit"
                        >로그아웃</button>
                    </form>
                    <Link to="/join">회원가입</Link>
                </div>) : (
                <div>
                    <Link to="/login">로그인</Link>
                    <br />
                    <Link to="/join">회원가입</Link>
                </div>
            )}
        </div>
    )
}

export default Home;