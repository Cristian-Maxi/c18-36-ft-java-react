import { useEffect, useState } from "react"
import { Link, Navigate } from 'react-router-dom'

import brain from "../../assets/brain.png"

import styles from './Login.module.css'

export const Login = () => {
  const [user, setUser] = useState({
    username: '',
    password: ''
  });
  const [csrfToken, setcsrfToken] = useState('')

  useEffect(() => {
    const fetchToken = async () => {
      try {
        const res = await fetch('http://localhost:8080/csrf-token', {
          method: 'GET',
          credentials: 'include'
        })
        const data = await res.json();
        setcsrfToken(data.token)
        console.log('CSRFToken: ', data.token);
      }
      catch (error) {
        console.error('Error al obtener el token CSRF');
      }
    }
    fetchToken()
  }, [])

  const catchInputs = (e) => {
    const { name, value } = e.target;
    const newUser = ({ ...user, [name]: value })
    setUser(newUser)
  }

  const logIn = async (e) => {
    e.preventDefault();
    const userDTO = {
      usuario: user.username,
      contrasenia: user.password
    }

    try {
      const res = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'aplication/json',
          'X-XSRF-TOKEN': csrfToken
        },
        credentials: 'include',
        body: JSON.stringify(userDTO)
      });

      const data = await res.json();
      const token = data.token;
      localStorage.setItem('jwt: ', token);
      Navigate('/dashboard')
    }

    catch (error) {
      console.error('Hubo un error al iniciar sesion: ', error);
    }
  }

  return (
    <>
      <img className={styles.brain}
        src={brain}
        alt="cerebro" />

      {/* WAVES */}
      <div className={styles.wave1} />
      <div className={styles.wave2} />
      <div className={styles.wave3} />

      <div className={styles.card}>
        <h1>Iniciar Sesion</h1>
        <form className={styles.form}
          onSubmit={logIn}>
          <input className={styles.input}
            placeholder="Usuario o correo electronico"
            type="text"
            name="user"
            onChange={catchInputs} />
          <input className={styles.input}
            placeholder="Contraseña"
            type="password"
            name="password"
            onChange={catchInputs} />
          <div className={styles.checkbox_container}>
            <input type="checkbox" />
            <span>Recordarme</span>
          </div>
          <button className={styles.button}>
            Ingresar
          </button>
        </form>
        <span>
          No tienes cuenta? <Link className={styles.link} to="createuser">Registrate</Link>
        </span>
      </div>
    </>

  )
}