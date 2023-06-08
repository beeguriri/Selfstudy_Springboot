import axios from "axios";
import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';

const Login = () => {

    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [showErrorMessage, setShowErrorMessage] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const navigate = useNavigate();

    let response = null;

    useEffect(() => {
        setIsLoggedIn(true);
    }, [response])

    const handleLogin = async (e) => {
        e.preventDefault();

        response = await axios
            .post('/api/user/login', {
                userid: id,
                password: password
            })
            .then((response) => {
                console.log(response.data); // 서버에서 반환한 데이터 출력
                setShowErrorMessage(false);
                sessionStorage.setItem('isLoggedIn', isLoggedIn)
                sessionStorage.setItem('userid', id)
                navigate('/success')
            })
            .catch((error) => {
                setShowErrorMessage(true);
                console.log(error);
            })
    };

    return (
        <>
            <div className="logindiv1">
                <form className="loginform" onSubmit={handleLogin}>
                    <div>
                        <input type="text" id="id" value={id} placeholder="아이디"
                            onChange={(e) => setId(e.target.value)} required />
                    </div>
                    <div>
                        <input type="password" id="password" value={password} placeholder="비밀번호"
                            onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit">로그인</button>
                    {showErrorMessage && (
                        <div className="logindiverror">ID 혹은 비밀번호가 일치하지 않습니다.</div>
                    )}
                </form>
                <Link to="/join">회원가입</Link>
            </div>

        </>
    )

}

export default Login;