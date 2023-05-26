import { Link } from "react-router-dom";

import React from "react";

const Home = () => {
    return (
        <div>
            홈화면 <br />
            sessionStorage: {sessionStorage.item}
            <ul>
                <li><Link to="/Login">로그인</Link></li>
                <li><Link to="/Join">회원가입</Link></li>
            </ul>
        </div>
    )
}

export default Home;