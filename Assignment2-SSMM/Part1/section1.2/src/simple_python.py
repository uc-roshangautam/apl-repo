#Simple python program to demonstrate the scope of variables
gbl_var = "Hello from global"

def demo():
    lcl_var = "Hello from local"
    print(f"In demo function - {lcl_var}")
    print(f"In demo function - {gbl_var}")

demo()
print(f"Outside - {gbl_var}")