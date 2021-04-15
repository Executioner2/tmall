<el-form :model="loginForm"
          autocomplete="on"
          :rules="rules"
          class="login-form">
  <div>
    <el-form-item prop="username" label="用户名">
      <el-input placeholder="请输入用户名"
                name="username"
                type="text"
                v-model="loginForm.username"
                autocomplete="on" />
    </el-form-item>
		<el-form-item prop="file"
              label="营业执照">
  		<el-upload class="fileupload"
              ref="upload"
              action="http://www.fiitool.com/upload"
              ::limit="1"
              :show-file-list="false"
              :on-change="PicturePreview"
              :auto-upload="false"
              accept="image/png,image/gif,image/jpg,image/jpeg">
    <i class="el-icon-upload avatar-uploader-icon"></i>
    <div v-show="!dialogImageUrl"
          slot="tip"
          class="el-upload__text upload__tip">上传照片</div>
 	 </el-upload>
	</el-form-item>
</el-form>
rules: {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: check.Username, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { validator: check.SimplePwd, trigger: 'blur' }
  ],
  company: [
    { required: true, message: '请输入公司名称', trigger: 'blur' },
    { validator: check.Company, trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: check.Phone, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: check.Email, trigger: 'blur' }
  ],
  file: [
    { required: true, message: '请上传营业执照' }
  ]
}
PicturePreview (file, fileList) {
  var URL = null
  if (window.createObjectURL !== undefined) {
    URL = window.createObjectURL(file.raw) // basic
  } else if (window.URL !== undefined) {
    URL = window.URL.createObjectURL(file.raw) // IE,google,360,Safari,firefox
  } else if (window.webkitURL !== undefined) {
    URL = window.webkitURL.createObjectURL(file.raw) // webkit
  }
  this.ImageUrl = URL
  this.registerForm.license = file.raw
  if (fileList.length > 0) {
    this.$refs['fileupload'].clearValidate() // 去掉file验证
  }
},
//CSS样式
.files {
  .el-form-item {
    width: 100%;
    height: 100%;
    text-align: center;
    /deep/ {
      .el-form-item__content {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        line-height: normal;
        padding: 5px;
        .fileupload {
          width: 100%;
          height: 100%;
          .uploader-icon {
            font-size: 40px;
            &:hover {
              color: royalblue;
            }
          }
        }
      }
    }
  }
}

