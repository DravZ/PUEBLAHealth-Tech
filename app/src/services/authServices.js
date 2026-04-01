const SECRET_KEY = "12345678901234567890123456789012"; // 32 chars = AES-256

const getKey = async () => {
  const encoder = new TextEncoder();
  return crypto.subtle.importKey(
    "raw",
    encoder.encode(SECRET_KEY),
    { name: "AES-GCM" },
    false,
    ["encrypt", "decrypt"],
  );
};

export const encrypt = async (text) => {
  const key = await getKey();
  const encoder = new TextEncoder();

  const iv = crypto.getRandomValues(new Uint8Array(12)); // IV seguro

  const encrypted = await crypto.subtle.encrypt(
    { name: "AES-GCM", iv },
    key,
    encoder.encode(text),
  );

  // Convertir a base64 para enviar
  const encryptedArray = new Uint8Array(encrypted);
  const encryptedBase64 = btoa(String.fromCharCode(...encryptedArray));

  const ivBase64 = btoa(String.fromCharCode(...iv));

  return {
    data: encryptedBase64,
    iv: ivBase64,
  };
};

export const registerUser = async (email, password) => {
  try {
    const response = await fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    });

    if (!response.ok) {
      throw new Error("Error al registrar usuario");
    }

    return await response.json();
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};

/*export const loginUser = async (email, password) => {
  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    });

    if (!response.ok) {
      throw new Error("Error al registrar usuario");
    }

    return await response.json();
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};*/

export const loginUser = async (email, password) => {
  try {
    // 🔐 cifrar ambos campos
    const encryptedEmail = await encrypt(email);
    const encryptedPassword = await encrypt(password);

    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: encryptedEmail.data,
        emailIv: encryptedEmail.iv,

        password: encryptedPassword.data,
        passwordIv: encryptedPassword.iv,
      }),
    });

    if (!response.ok) {
      throw new Error("Error al iniciar sesión");
    }

    return await response.json();
  } catch (error) {
    console.error("Error:", error);
    throw error;
  }
};
