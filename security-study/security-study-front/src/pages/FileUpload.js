import axios from "axios";

const FileUpload = () => {

    const fileList = []

    const onSaveFile = (e) => {

        const uploadFile = Array.prototype.slice.call(e.target.files);

        uploadFile.forEach((element) => {
            fileList.push(element);
        });
    }

    const onFileUpload = () => {
        const formData = new FormData();

        // JSON 형식으로 파싱 후 추가
        formData.append('userid', new Blob([JSON.stringify(sessionStorage.getItem('userid'))] , {type: "application/json"})); 

        fileList.forEach((file) => {

            formData.append('multipartFiles', file);
        })

        axios({
            method: "POST",
            url: `api/uploadFiles`,
            mode: "cors",
            headers: {
              "Content-Type": "multipart/form-data", // Content-Type을 반드시 이렇게 하여야 한다.
            },
            data: formData,

        }).then((response) => {
            console.log("response 왔어용")
            console.log(response.data)
        })
    }

    return (
        <div>
            <input type="file" multiple onChange={onSaveFile} />
            <button onClick={onFileUpload}>파일 업로드</button>
        </div>
    )
}

export default FileUpload;