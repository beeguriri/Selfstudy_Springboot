import axios from "axios";
import { useState } from "react";

const Join = () => {

    const [joinId, setJoinId] = useState("");
    const [joinPw, setJoinPw] = useState("");
    const [validPw, setValidPw] = useState("");


    const handleJoinId = (e) => {
        // console.log('handleInputId', e.target.value)
        setJoinId(e.target.value);
    };

    const handleJoinPw = (e) => {
        // console.log('handleInputPw', e.target.value)
        setJoinPw(e.target.value);
    };

    const handleValidPw = (e) => {
        setValidPw(e.target.value);
    }

    const onClickJoin = async () => {
        console.log("click login");
        console.log("ID : ", joinId);
        console.log("PW : ", joinPw);

        await axios
            .post("http://localhost:8080/api/join", {
                userid: joinId,
                password: joinPw,
            })
            .then((res) => {
                console.log(res);
            })
            .catch(
            );
    };

    return (
        <div>
            로그인 안해도 볼수 있는 회원가입 화면 <br />
            <br /><br />
            <input
                type="text"
                className="form-control"
                placeholder="아이디 입력"
                name="input_id"
                value={joinId}
                onChange={handleJoinId}
            />
            <br /><br />
            <input
                type="password"
                className="form-control"
                placeholder="비밀번호 입력"
                name="input_pw"
                value={joinPw}
                onChange={handleJoinPw}
            />
            <input
                type="password"
                className="form-control"
                placeholder="비밀번호 입력"
                name="input_pw"
                value={validPw}
                onChange={handleValidPw}
            />
            <br /><br />
            <button
                type="button"
                onClick={onClickJoin}
            >회원가입</button>

        </div>
    )

}

export default Join