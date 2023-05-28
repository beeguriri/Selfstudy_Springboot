import { Link } from "react-router-dom";
import { useState, useEffect } from "react";

const Home = () => {

    const [isLogin, setIsLogin] = useState(false);

    useEffect(() => {

        if (sessionStorage.getItem("user_id") === null) {
            // sessionStorage 에 user_id 라는 key 값으로 저장된 값이 없다면
            console.log("isLogin ?? :: ", isLogin);
        } else {
            // sessionStorage 에 user_id 라는 key 값으로 저장된 값이 있다면
            // 로그인 상태 변경
            setIsLogin(true);
            console.log("isLogin ?? :: ", isLogin);
        }
    });

    const logoutEvent = () => {
        sessionStorage.removeItem("user_id")
        setIsLogin(false);
    }
    
    return (
        // <div>
        //     홈화면 <br />
        //     {isLogin ? (
        //         <ul>
        //             <li><Link to="/logout" onClick={logoutEvent}>로그아웃</Link></li>
        //         </ul>
        //     ) : (
        //         <ul>
        //             <li><Link to="/login">로그인</Link></li>
        //             <li><Link to="/join">회원가입</Link></li>
        //             <li><Link to="/success">로그인여부 확인</Link></li>
        //         </ul>
        //     )}
        // </div>
        <div>
            
        </div>


    )
}

export default Home;