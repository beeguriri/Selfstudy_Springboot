import axios from "axios";
import { useState } from "react";

const Login = () => {

    const [inputId, setInputId] = useState("");
    const [inputPw, setInputPw] = useState("");

    const handleInputId = (e) => {
        // console.log('handleInputId', e.target.value)
        setInputId(e.target.value);
    };

    const handleInputPw = (e) => {
        // console.log('handleInputPw', e.target.value)
        setInputPw(e.target.value);
    };

    const onClickLogin = async () => {
        console.log("click login");
        console.log("ID : ", inputId);
        console.log("PW : ", inputPw);

        await axios
            .post("http://localhost:8080/api/login", {
                userid: inputId,
                password: inputPw,
            })
            .then((res) => {
                console.log("res", res);
                if (res.data.userid === null) {
                    // id 일치하지 않는 경우
                    alert("일치하는 아이디가 없습니다.");
                } else if (res.data.userid === inputId && res.data.password === null) {
                    alert("비밀번호가 틀렸습니다.");
                } else {
                    alert("로그인 성공");
                    sessionStorage.setItem("user_id", inputId); // sessionStorage에 id를 user_id라는 key 값으로 저장
                    document.location.href = "/success";
                }
                // // 작업 완료 되면 페이지 이동(새로고침)
                // document.location.href = "/success";
            })
            .catch(
            );
    };

    return (
        <>
            로그인화면<br />
            <br /><br />
            <input
                type="text"
                className="form-control"
                placeholder="아이디 입력"
                name="input_id"
                value={inputId}
                onChange={handleInputId}
            />
            <br /><br />
            <input
                type="password"
                className="form-control"
                placeholder="비밀번호 입력"
                name="input_pw"
                value={inputPw}
                onChange={handleInputPw}
            />
            <br /><br />
            <button
                type="button"
            onClick={onClickLogin}
            >로그인</button>

        </>
    )

}

export default Login;