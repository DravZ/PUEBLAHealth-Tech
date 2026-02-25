import { useState } from "react";

const Registro = () => {
  const [form, setForm] = useState({
    email: "",
    password: "",
    role: "MEDICO",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log(form); // listo para backend
  };

  return (
    <div className="container">
      <div className="card">
        <h1>Crear Cuenta</h1>
        <p className="subtitle">Registro de personal médico</p>

        <form onSubmit={handleSubmit}>
          <div className="inputGroup">
            <label>Correo electrónico</label>
            <input
              type="email"
              name="email"
              placeholder="ejemplo@hospital.com"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="inputGroup">
            <label>Contraseña</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="inputGroup">
            <label>Rol</label>
            <select name="role" value={form.role} onChange={handleChange}>
              <option value="MEDICO">Médico</option>
            </select>
          </div>

          <button type="submit">Registrarse</button>
        </form>
      </div>

      <style>{`
        .container {
          height: 98vh;
          display: flex;
          justify-content: center;
          align-items: center;
          background: linear-gradient(135deg, #1f4037, #99f2c8);
          font-family: 'Segoe UI', sans-serif;
        }

        .card {
          background: white;
          padding: 45px;
          width: 380px;
          border-radius: 18px;
          box-shadow: 0 20px 40px rgba(0,0,0,0.15);
          animation: fadeIn 0.5s ease-in-out;
        }

        h1 {
          margin: 0;
          font-size: 26px;
          color: #1f4037;
        }

        .subtitle {
          margin-bottom: 25px;
          color: #777;
          font-size: 14px;
        }

        .inputGroup {
          display: flex;
          flex-direction: column;
          margin-bottom: 18px;
        }

        label {
          font-size: 13px;
          margin-bottom: 6px;
          color: #444;
        }

        input, select {
          padding: 12px;
          border-radius: 10px;
          border: 1px solid #ddd;
          font-size: 14px;
          transition: all 0.2s ease;
        }

        input:focus, select:focus {
          outline: none;
          border-color: #1f4037;
          box-shadow: 0 0 0 3px rgba(31,64,55,0.15);
        }

        button {
          width: 100%;
          padding: 14px;
          margin-top: 10px;
          border: none;
          border-radius: 12px;
          background: #1f4037;
          color: white;
          font-weight: bold;
          font-size: 15px;
          cursor: pointer;
          transition: 0.3s ease;
        }

        button:hover {
          background: #163029;
          transform: translateY(-2px);
        }

        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }
      `}</style>
    </div>
  );
};

export default Registro;
