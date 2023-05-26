import axios from "axios";
import { useState, useEffect, useRef } from "react";

const Main = () => {

    const [inputId, setInputId] = useState("");
    const [inputPw, setInputPw] = useState("");

    const handleInputId = (e) => {
        console.log('handleInputId', e.target.value)
        setInputId(e.target.value);
    };

    const handleInputPw = (e) => {
        console.log('handleInputPw', e.target.value)
        setInputPw(e.target.value);
    };

    const onClickLogin = () => {
        console.log("click login");
        console.log("ID : ", inputId);
        console.log("PW : ", inputPw);
        axios
            .post("http://localhost:8080/api/login", {
                email: inputId,
                passwd: inputPw,
            })
            .then((res) => {
                console.log(res);
                console.log("res.data.userId :: ", res.data.userId);
                console.log("res.data.msg :: ", res.data.msg);        
                if (res.data.email === undefined) {
                    // id 일치하지 않는 경우 userId = undefined, msg = '입력하신 id 가 일치하지 않습니다.'
                    console.log("======================", res.data.msg);
                    alert("입력하신 id 가 일치하지 않습니다.");
                } else if (res.data.email === null) {
                    // id는 있지만, pw 는 다른 경우 userId = null , msg = undefined
                    console.log(
                        "======================",
                        "입력하신 비밀번호 가 일치하지 않습니다."
                    );
                    alert("입력하신 비밀번호 가 일치하지 않습니다.");
                } else if (res.data.email === inputId) {
                    // id, pw 모두 일치 userId = userId1, msg = undefined
                    console.log("======================", "로그인 성공");
                    sessionStorage.setItem("user_id", inputId); // sessionStorage에 id를 user_id라는 key 값으로 저장
                    sessionStorage.setItem("name", res.data.name); // sessionStorage에 id를 user_id라는 key 값으로 저장
                }
                // 작업 완료 되면 페이지 이동(새로고침)
                document.location.href = "/";
            })
            .catch();
    };

    return (
        <>
            로그인화면
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

export default Main;