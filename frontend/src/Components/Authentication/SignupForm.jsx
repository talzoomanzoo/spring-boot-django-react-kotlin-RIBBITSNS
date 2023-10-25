import {
  Button,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React from "react";
import { useDispatch } from "react-redux";
import * as Yup from "yup";
import { registerUser } from "../../Store/Auth/Action";

const validationSchema = Yup.object().shape({
  fullName: Yup.string().required("필수 항목입니다."),
  email: Yup.string().email("잘못된 이메일").required("필수 항목입니다."),
  password: Yup.string()
    .required("필수 항목입니다.")
    .min(6, "비밀번호는 6자리 이상 가능합니다."),
});

const days = Array.from({ length: 31 }, (_, i) => i + 1);
const months = [
  { value: "01", label: "1월" },
  { value: "02", label: "2월" },
  { value: "03", label: "3월" },
  { value: "04", label: "4월" },
  { value: "05", label: "5월" },
  { value: "06", label: "6월" },
  { value: "07", label: "7월" },
  { value: "08", label: "8월" },
  { value: "09", label: "9월" },
  { value: "10", label: "10월" },
  { value: "11", label: "11월" },
  { value: "12", label: "12월" },
];
const currentYear = new Date().getFullYear();
const years = Array.from({ length: 100 }, (_, i) => currentYear - i);

const SignupForm = () => {
  const dispatch = useDispatch();


  const formik = useFormik({
    initialValues: {
      fullName: "",
      email: "",
      password: "",
      birthDate: {
        day: "",
        month: "",
        year: "",
      },
    },
    validationSchema,
    onSubmit: (values) => {
      const { day, month, year } = values.birthDate;
      const birthDate = `${year}-${month}-${day}`;
      values.birthDate = birthDate;

      console.log("values", values);
      dispatch(registerUser(values))
    },
  });

  const handleDateChange = (name) => (event) => {
    formik.setFieldValue("birthDate", {
      ...formik.values.birthDate,
      [name]: event.target.value,
    });
  };

  return (
    <form onSubmit={formik.handleSubmit}>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <TextField
            name="fullName"
            label="이름"
            fullWidth
            variant="outlined"
            size="large"
            value={formik.values.fullName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.fullName && Boolean(formik.errors.fullName)}
            helperText={formik.touched.fullName && formik.errors.fullName}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            className="w-full"
            name="email"
            label="이메일"
            fullWidth
            variant="outlined"
            size="large"
            value={formik.values.email}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.email && Boolean(formik.errors.email)}
            helperText={formik.touched.email && formik.errors.email}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            name="password"
            label="비밀번호"
            fullWidth
            variant="outlined"
            size="large"
            type="password"
            value={formik.values.password}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.password && Boolean(formik.errors.password)}
            helperText={formik.touched.password && formik.errors.password}
          />
        </Grid>
        <Grid item xs={4}>
          <InputLabel>일</InputLabel>
          <Select
            name="day"
            value={formik.values.birthDate.day}
            onChange={handleDateChange("day")}
            onBlur={formik.handleBlur}
            error={
              formik.touched.birthDate && Boolean(formik.errors.birthDate)
            }
            className="w-full"
          >
            {days.map((day) => (
              <MenuItem key={day} value={day}>
                {day}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item xs={4}>
          <InputLabel>월</InputLabel>
          <Select
            name="month"
            value={formik.values.birthDate.month}
            onChange={handleDateChange("month")}
            onBlur={formik.handleBlur}
            error={
              formik.touched.birthDate && Boolean(formik.errors.birthDate)
            }
            className="w-full"
          >
            {months.map((month) => (
              <MenuItem key={month.value} value={month.value}>
                {month.label}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item xs={4}>
          <InputLabel>년</InputLabel>
          <Select
            name="year"
            value={formik.values.birthDate.year}
            onChange={handleDateChange("year")}
            onBlur={formik.handleBlur}
            error={
              formik.touched.birthDate && Boolean(formik.errors.birthDate)
            }
            className="w-full"
          >
            {years.map((year) => (
              <MenuItem key={year} value={year}>
                {year}
              </MenuItem>
            ))}
          </Select>
        </Grid>
        <Grid item xs={12}>
          {formik.touched.birthDate && formik.errors.birthDate && (
            <div className="text-red-500">{formik.errors.birthDate}</div>
          )}
        </Grid>
        <Grid className="mt-20" item xs={12}>
          <Button
            type="submit"
            sx={{
              width: "100%",
              borderRadius: "29px",
              py: "15px",
              bgcolor: "#008000",
            }}
            variant="contained"
            size="large"
          >
            가입하기
          </Button>
        </Grid>
      </Grid>
    </form>
  );
};

export default SignupForm;
