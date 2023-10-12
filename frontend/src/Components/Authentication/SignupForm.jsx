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
  fullName: Yup.string().required("Full Name is required"),
  email: Yup.string().email("Invalid email").required("Email is required"),
  password: Yup.string()
    .required("Password is required")
    .min(6, "Password must be at least 6 characters"),
});

const days = Array.from({ length: 31 }, (_, i) => i + 1);
const months = [
  { value: "01", label: "January" },
  { value: "02", label: "February" },
  { value: "03", label: "March" },
  { value: "04", label: "April" },
  { value: "05", label: "May" },
  { value: "06", label: "June" },
  { value: "07", label: "July" },
  { value: "08", label: "August" },
  { value: "09", label: "September" },
  { value: "10", label: "October" },
  { value: "11", label: "November" },
  { value: "12", label: "December" },
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
            label="Full Name"
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
            label="Email"
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
            label="Password"
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
          <InputLabel>Date</InputLabel>
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
          <InputLabel>Month</InputLabel>
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
          <InputLabel>Year</InputLabel>
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
              bgcolor: "#1d9bf0",
            }}
            variant="contained"
            size="large"
          >
            Signup
          </Button>
        </Grid>
      </Grid>
    </form>
  );
};

export default SignupForm;
